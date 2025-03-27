import {Dayjs} from "dayjs";

export interface RowData {
    id: number;
    name: string;
    coordinates: Coordinates;
    creationDate: Date;
    genre: Genre;
    numberOfParticipants: number;
    singlesCount: number;
    description: string;
    bestAlbum: BestAlbum;
    albumsCount: number;
    establishmentDate: Dayjs;
    studio: Studio;
    createdBy: number;
}

export enum Genre {
    RAP = 'RAP',
    SOUL = 'SOUL',
    POST_ROCK = 'POST_ROCK'

}

export interface Coordinates {
    id: number;
    x: number;
    y: number;
}

export interface BestAlbum {
    id: number;
    name: string;
    tracks: number;
    length: number;
    sales: number;
}

export interface Studio {
    id: number,
    name: string,
    createdBy: number
}


export enum AccessRights {
    USER = 'USER',
    ADMIN = 'ADMIN'
}

export interface RegisterData {
    username: string;
    password: string;
    roles: AccessRights[]
}

export interface importHistoryData {
    id: number,
    status: string
    userId: number,
    objectsCount: number,
    fileName: string
}