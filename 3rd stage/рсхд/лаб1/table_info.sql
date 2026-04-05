BEGIN;

-- чекаем права на доступ
DO
$$
    BEGIN
        IF NOT has_schema_privilege(current_schema(), 'CREATE') THEN
            RAISE EXCEPTION 'у вас нет прав на CREATE для текущей бд =(';
        END IF;

        RAISE NOTICE 'у вас достаточно прав на CREATE =)';
    END;
$$;

CREATE OR REPLACE PROCEDURE get_table_info(full_path text) AS
$$
DECLARE
    schema_name    TEXT;
    table_name     TEXT;
    row            RECORD;
    index_row      RECORD;
    row_counter    INT := 1;
BEGIN
    IF position('.' IN full_path) > 0 THEN
        table_name := split_part(full_path, '.', length(full_path) - length(replace(full_path, '.', '')) + 1);
        schema_name := split_part(full_path, '.', length(full_path) - length(replace(full_path, '.', '')));
    ELSE
        schema_name := current_schema();
        table_name := full_path;
    END IF;

    IF EXISTS(SELECT tablename
              FROM pg_tables
              WHERE schemaname = schema_name
                AND tablename = table_name)
    THEN
        RAISE NOTICE E'\rТаблица: %.%', schema_name, table_name;
        RAISE NOTICE E'\r%', repeat(' ', 7);
    ELSE
        RAISE NOTICE E'\rТаблица ''%.%'' не найдена', schema_name, table_name;
        RETURN;
    END IF;

    -- чекаем что существует связь
    IF EXISTS(SELECT tablename
              FROM pg_tables
              WHERE schemaname = schema_name
                AND tablename = table_name)
    THEN
        RAISE NOTICE E'\rТаблица: %', full_path;
        RAISE NOTICE E'\r%', repeat(' ', 7);
    ELSE
        RAISE NOTICE E'\rТаблица ''%'' не найдена', full_path;
        RETURN;
    END IF;

    RAISE NOTICE E'\rNo. Имя столбца       Атрибуты';
    RAISE NOTICE E'\r% % %',
        repeat('-', 3),
        repeat('-', 17),
        repeat('-', 54);

    FOR row IN (SELECT DISTINCT a.attname,
                       a.attnum,
                       t.typname,
                       a.atttypmod,
                       a.attnotnull,
                       d.description
                FROM pg_attribute AS a
                         JOIN pg_type AS t ON a.atttypid = t.oid
                         LEFT JOIN pg_description AS d ON (d.objoid = full_path::regclass AND d.objsubid = a.attnum)
                WHERE a.attrelid = full_path::regclass
                  AND a.attnum > 0
    )
        LOOP
            RAISE NOTICE E'\r% % Type     :   % %',
                rpad(row_counter::text, 4),
                rpad(row.attname, 17),
                CASE WHEN row.atttypmod != -1 THEN row.typname || '(' || (row.atttypmod) || ')' ELSE row.typname END,
                CASE WHEN row.attnotnull THEN 'Not null' ELSE '' END;

            -- вывод комментария
            RAISE NOTICE E'\r% Commen   :   %',
                repeat(' ', 22),
                COALESCE(row.description, '');

            -- вывод индекса
            RAISE NOTICE E'\r% Index    :   %',
                repeat(' ', 22),
                COALESCE((SELECT string_agg(indexrelid::regclass::text, ', ')
                          FROM pg_index
                          WHERE indrelid = full_path::regclass
                            AND row.attnum = ANY (indkey)), '');

            row_counter := row_counter + 1;
        END LOOP;

END;
$$ LANGUAGE plpgsql;

\prompt 'Введите название таблицы: ' name
CALL get_table_info(:'name');

COMMIT;