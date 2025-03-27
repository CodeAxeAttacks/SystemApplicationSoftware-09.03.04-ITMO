import requests
import csv


TOTAL_GAMES = 400  # actual count = 298
TRAIN_SET_GAMES = 235  # ~0.8

API_KEY = "9b25260cec3c4e10b3a64c39cf04ef37"
PAGE_SIZE = 40


def fetch_data(endpoint, params=None):
    url = f"https://api.rawg.io/api/{endpoint}"
    if not params:
        params = {}
    params['key'] = API_KEY
    response = requests.get(url, params=params)
    if response.status_code == 200:
        return response.json().get('results', [])
    return []


def get_genres():
    return [genre['name'] for genre in fetch_data("genres")]


def get_platforms():
    return [platform['name'] for platform in fetch_data("platforms")]


def get_games(count=TOTAL_GAMES):
    games = []
    pages_needed = (count + PAGE_SIZE - 1) // PAGE_SIZE
    for page in range(1, pages_needed + 1):
        params = {"page": page, "page_size": PAGE_SIZE}
        games.extend(fetch_data("games", params))
        if len(games) >= count:
            break
    return games[:count]


genres = get_genres()
platforms = get_platforms()
games_data = get_games()

triplets = []
rows = []


def process_game(game, is_training_set):
    if game.get("metacritic") is None or game.get("esrb_rating") is None or not(1 <= game.get('playtime', 0) <= 120):
        return
    
    game_name = game.get("name")
    
    row = {
        "name": game_name,
        "genre": None,
        "release_year": None,
        "rating": None,
        "metacritic_score": None,
        "multiplayer": 0,
        "online": 0,
        "ratings_count": None,
        "playtime": None,
        "age_rating": None,
    }

    if game_genres := game.get('genres'):
        genre_name = game_genres[0]['name']
        row['genre'] = genre_name
        if is_training_set:
            triplets.append((game_name, "has_genre", genre_name))

    if released := game.get('released'):
        release_year = released[:4]
        row['release_year'] = release_year
        if is_training_set:
            triplets.append((game_name, "has_release_year", release_year))

    if rating := game.get('rating'):
        row['rating'] = rating
        if is_training_set:
            triplets.append((game_name, "has_rating", rating))
    if metacritic := game.get('metacritic'):
        row['metacritic_score'] = metacritic
        if is_training_set:
            triplets.append((game_name, "has_metacritic_score", metacritic))

    tags = [tag['name'].lower() for tag in game.get('tags', [])]
    if any('online' in tag for tag in tags):
        row['online'] = 1
        if is_training_set:
            triplets.append((game_name, "is_online", 1))
    if any('multiplayer' in tag or 'co-op' in tag or 'cooperative' in tag for tag in tags):
        row['multiplayer'] = 1
        if is_training_set:
            triplets.append((game_name, "is_multiplayer", 1))

    if ratings_count := game.get('ratings_count'):
        row['ratings_count'] = ratings_count
        if is_training_set:
            triplets.append((game_name, "has_ratings_count", ratings_count))
    if playtime := game.get('playtime'):
        row['playtime'] = playtime
        if is_training_set:
            triplets.append((game_name, "has_playtime", playtime))
    if age_rating := game.get('esrb_rating', {}).get('name'):
        row['age_rating'] = age_rating
        if is_training_set:
            triplets.append((game_name, "has_age_rating", age_rating))

    rows.append(row)


for index, game in enumerate(games_data):
    is_training_set = index < TRAIN_SET_GAMES
    process_game(game, is_training_set)

with open('dataaa.csv', mode='w', newline='', encoding='utf-8') as file:
    writer = csv.DictWriter(file, fieldnames=rows[0].keys())
    writer.writeheader()
    writer.writerows(rows)

with open('triplets_train.txt', mode='w', encoding='utf-8') as file:
    file.write(str(triplets))
    
    