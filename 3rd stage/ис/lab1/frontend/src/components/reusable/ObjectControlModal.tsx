import {
    Box,
    Button,
    Dialog,
    DialogContent,
    DialogTitle, FormControl,
    InputLabel,
    MenuItem,
    Select, SelectChangeEvent, Stack, Switch,
    TextField,
    Typography
} from "@mui/material";
import React, {useEffect, useState} from "react";
import {
    Coordinates,
    RowData,
    Genre, BestAlbum, Studio
} from "../../interfaces.ts";
import axiosInstance from "../../axiosConfig.ts";
import Notification from "./Notification.tsx";
import {LocalizationProvider, DatePicker} from '@mui/x-date-pickers';
import {AdapterDayjs} from '@mui/x-date-pickers/AdapterDayjs';
import dayjs, {Dayjs} from "dayjs";


interface ModalProps {
    modalOpen: boolean;
    onModalCLose: () => void;
    chosenObject?: RowData | undefined,
    isNewBand: boolean;
    onSendError: () => void;
    readonlyForCurrentUser: boolean
}


const ObjectControlModal: React.FC<ModalProps> = ({
                                                      modalOpen,
                                                      onModalCLose,
                                                      chosenObject,
                                                      isNewBand,
                                                      onSendError,
                                                      readonlyForCurrentUser
                                                  }) => {

    const [name, setName] = useState<string | undefined>(chosenObject?.name);
    const [coordinates, setCoordinates] = useState<Coordinates | undefined>(chosenObject?.coordinates);
    const [genre, setGenre] = useState<Genre | undefined>(chosenObject?.genre);
    const [numberOfParticipants, setNumberOfParticipants] = useState<number | undefined>(chosenObject?.numberOfParticipants);
    const [singlesCount, setSinglesCount] = useState<number | undefined>(chosenObject?.singlesCount);
    const [description, setDescription] = useState<string | undefined>(chosenObject?.description);
    const [bestAlbum, setBestAlbum] = useState<BestAlbum | undefined>(chosenObject?.bestAlbum);
    const [albumsCount, setAlbumsCount] = useState<number | undefined>(chosenObject?.albumsCount);
    const [establishmentDate, setEstablishmentDate] = useState<Dayjs | null | undefined>(chosenObject?.establishmentDate);
    const [studio, setStudio] = useState<Studio | undefined>(chosenObject?.studio)
    const [isAlbumEnabled, setIsAlbumEnabled] = useState(!!chosenObject?.bestAlbum);
    const [requestError, setRequestError] = useState<boolean>(false)
    const [existingStudios, setExistingStudios] = useState<Studio[]>([])
    const [selectExistingStudios, setSelectExistingStudios] = useState<boolean>(false)

    const [errors, setErrors] = useState<{ stringError?: string; numberError?: string; }>({});


    useEffect(() => {
        axiosInstance.get('api/studios')
            .then((response) => {
                setExistingStudios(response.data)
            })
            .catch(() => {
                setRequestError(true)
                setExistingStudios([])
            })

        setExistingStudios([])
    }, []);

    const handleClose = () => {
        setSelectExistingStudios(false)
        setErrors((prevErrors) => ({
            ...prevErrors,
            numberError: undefined,
            stringError: undefined,
        }));
        onModalCLose()
    }

    const handleNotificationClose = () => {
        setRequestError(false)
    }

    const handleObjectFormSubmit = async (event: React.FormEvent) => {
        event.preventDefault()

        const requestData = {
            "name": name,
            "coordinates": {
                "x": coordinates?.x,
                "y": coordinates?.y,
            },
            "genre": genre,
            "numberOfParticipants": numberOfParticipants,
            "singlesCount": singlesCount,
            "description": description,
            "bestAlbum": isAlbumEnabled
                ? {
                    "name": bestAlbum?.name,
                    "tracks": bestAlbum?.tracks,
                    "length": bestAlbum?.length,
                    "sales": bestAlbum?.sales,
                }
                : null,
            "albumsCount": albumsCount,
            "establishmentDate": dayjs(establishmentDate).toISOString(),
            "studio": studio ? { "name": studio.name } : null, // Если студия не выбрана, отправить null
        }


        if (isNewBand) {
            await axiosInstance.post('api/music-bands', requestData, {params: {studioId: (selectExistingStudios ? studio?.id : -1)}})
                .catch(() => {
                    onSendError()
                })

        } else {
            await axiosInstance.put(`api/music-bands/${chosenObject?.id}`, requestData, {params: {studioId: (selectExistingStudios ? studio?.id : -1)}})
                .catch(() => {
                    onSendError()
                })
        }

        setSelectExistingStudios(false)
        setErrors((prevErrors) => ({
            ...prevErrors,
            numberError: undefined,
            stringError: undefined,
        }));
        onModalCLose()
    }


    const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newName = event.target.value
        setName(newName);

        if (newName.trim().length < 1) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                stringError: "Name can't be empty!",
            }));
        } else {
            setErrors((prevErrors) => ({...prevErrors, stringError: undefined}));
        }
    };

    const handleCoordinatesChange = (field: keyof Coordinates, value: number) => {
        // @ts-ignore
        setCoordinates((prev) => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleNumberOfParticipantsChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setNumberOfParticipants(Number(event.target.value));

        if (Number(event.target.value) <= 0 || parseInt(event.target.value) !== Number(event.target.value)) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                studentsCountError: "Only positive integer numbers!",
            }));
        } else {
            setErrors((prevErrors) => ({...prevErrors, studentsCountError: undefined}));
        }
    };

    const handleSinglesCountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (Number(event.target.value) <= 0 || parseInt(event.target.value) !== Number(event.target.value)) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                expelledStudentsError: "Only positive integer numbers!",
            }));
        } else {
            setErrors((prevErrors) => ({...prevErrors, expelledStudentsError: undefined}));
        }
        setSinglesCount(Number(event.target.value));
    };

    const handleAlbumSwitch = (event: React.ChangeEvent<HTMLInputElement>) => {
        setIsAlbumEnabled(event.target.checked);
    };

    const handleDescriptionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setDescription(event.target.value);
    };

    const handleGenreChange = (event: SelectChangeEvent) => {
        setGenre(event.target.value as Genre);
    };

    const handleAlbumsCountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setAlbumsCount(Number(event.target.value));
        if (Number(event.target.value) <= 0 || parseInt(event.target.value) !== Number(event.target.value)) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                shouldBeExpelledError: "Only positive integer numbers!",
            }));
        } else {
            setErrors((prevErrors) => ({...prevErrors, shouldBeExpelledError: undefined}));
        }
    };

    const handleBestAlbumChange = (field: keyof BestAlbum, value: any) => {
        // @ts-ignore
        setBestAlbum((prev) => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleStudioChoosing = (event: SelectChangeEvent) => {
        if (event.target.value === '') {
            setStudio(undefined)
        } else {
            setStudio(existingStudios.find(a => a.id === Number(event.target.value)))
        }
    }

    const handleStudioChange = (field: keyof Studio, value: any) => {
        // @ts-ignore
        setStudio((prev) => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleEstablishmentDateChange = (newDate: Dayjs | null) => {
        setEstablishmentDate(newDate)
    }

    return (
        <Dialog open={modalOpen} onClose={handleClose} sx={{'& .MuiDialog-paper': {width: '600px', maxWidth: 'none'}}}>
            {!isNewBand && <DialogTitle>Edit
                band {chosenObject?.name} {readonlyForCurrentUser ? '(readonly access)' : ''}</DialogTitle>}
            {isNewBand && <DialogTitle>Create new band</DialogTitle>}

            <DialogContent>
                <Box
                    sx={{display: 'flex', flexDirection: 'column', gap: '20px'}}
                    component="form"
                    onSubmit={handleObjectFormSubmit}
                >
                    {!isNewBand && <TextField
                        label="Id"
                        disabled
                        defaultValue={isNewBand ? undefined : chosenObject?.id}
                        sx={{marginTop: '5px'}}
                    ></TextField>}


                    <TextField
                        label="Name"
                        required
                        defaultValue={isNewBand ? undefined : chosenObject?.name}
                        onChange={handleNameChange}
                        error={!!errors.stringError}
                        helperText={errors.stringError}
                        disabled={readonlyForCurrentUser}
                        sx={{marginTop: '5px'}}
                    ></TextField>

                    <Box sx={{display: 'flex', flexDirection: 'column'}}>
                        <Typography sx={{marginBottom: '10px'}}>
                            Coordinates
                        </Typography>

                        <FormControl>
                            <Box sx={{display: 'flex', flexDirection: 'row', gap: '5px'}}>
                                <TextField
                                    label="X"
                                    type="number"
                                    defaultValue={isNewBand ? undefined : chosenObject?.coordinates.x}
                                    sx={{ marginRight: '5px' }}
                                    onChange={(e) => handleCoordinatesChange('x', parseFloat(e.target.value) || 0)} // Используем parseFloat
                                    required
                                    disabled={readonlyForCurrentUser}
                                    inputProps={{
                                        step: "any", // Позволяет вводить дробные значения в поле
                                    }}
                                />

                                <TextField
                                    label="Y"
                                    type="number"
                                    defaultValue={isNewBand ? undefined : chosenObject?.coordinates.y}
                                    onChange={(e) => handleCoordinatesChange('y', Number(e.target.value))}
                                    required
                                    disabled={readonlyForCurrentUser}
                                ></TextField>
                            </Box>
                        </FormControl>

                    </Box>

                    {!isNewBand && <TextField
                        disabled
                        label="Creation date"
                        defaultValue={isNewBand ? undefined : chosenObject?.creationDate.toLocaleString()}
                    ></TextField>}


                    <InputLabel>Genre</InputLabel>
                    <Select
                        defaultValue={isNewBand ? undefined : chosenObject?.genre}
                        label="Genre"
                        variant="standard"
                        required
                        disabled={readonlyForCurrentUser}
                        onChange={handleGenreChange}
                    >
                        {Object.values(Genre).map((form, index) => (
                            <MenuItem value={form} key={index}>
                                {form}
                            </MenuItem>
                        ))}
                    </Select>

                    <TextField
                        label="Number of participants"
                        type="number"
                        required
                        defaultValue={isNewBand ? undefined : chosenObject?.numberOfParticipants}
                        onChange={handleNumberOfParticipantsChange}
                        error={!!errors.numberError}
                        helperText={errors.numberError}
                        inputProps={{
                            min: 1,
                            step: 1,
                            pattern: "^[1-9][0-9]*$", // Только положительные целые числа
                        }}
                        disabled={readonlyForCurrentUser}
                    ></TextField>

                    <TextField
                        label="Singles count"
                        type="number"
                        required
                        defaultValue={isNewBand ? undefined : chosenObject?.singlesCount}
                        onChange={handleSinglesCountChange}
                        error={!!errors.numberError}
                        helperText={errors.numberError}
                        inputProps={{
                            min: 1,
                            step: 1,
                            pattern: "^[1-9][0-9]*$", // Только положительные целые числа
                        }}
                        disabled={readonlyForCurrentUser}
                    ></TextField>

                    <TextField
                        label="Description"
                        type="text"
                        required
                        defaultValue={isNewBand ? undefined : chosenObject?.description}
                        onChange={handleDescriptionChange}
                        error={!!errors.stringError}
                        helperText={errors.stringError}
                        disabled={readonlyForCurrentUser}
                    ></TextField>
                    <Stack direction="row" spacing={1} sx={{alignItems: 'center'}}>
                    <Typography>Is album needed?</Typography>
                    <Switch
                        checked={isAlbumEnabled}
                        onChange={handleAlbumSwitch}
                        defaultValue={'true'}
                        inputProps={{'aria-label': 'controlled'}}
                        disabled={readonlyForCurrentUser}
                    />
                    </Stack>
                    {isAlbumEnabled && <Box className="form-box-container">
                        <Typography sx={{marginBottom: '10px'}}>Album</Typography>
                        <TextField
                            label="Name"
                            required
                            defaultValue={isNewBand ? undefined : chosenObject?.bestAlbum?.name}
                            onChange={(e) => handleBestAlbumChange('name', e.target.value)}
                            disabled={readonlyForCurrentUser}
                        ></TextField>

                        <TextField
                            label="Tracks"
                            type="number"
                            required
                            defaultValue={isNewBand ? undefined : chosenObject?.bestAlbum?.tracks}
                            onChange={(e) => handleBestAlbumChange('tracks', Number(e.target.value))}
                            error={!!errors.numberError}
                            helperText={errors.numberError}
                            inputProps={{
                                min: 1,
                                step: 1,
                                pattern: "^[1-9][0-9]*$", // Только положительные целые числа
                            }}
                            disabled={readonlyForCurrentUser}
                        ></TextField>

                        <TextField
                            label="Length"
                            type="number"
                            required
                            defaultValue={isNewBand ? undefined : chosenObject?.bestAlbum?.length}
                            onChange={(e) => handleBestAlbumChange('length', Number(e.target.value))}
                            error={!!errors.numberError}
                            helperText={errors.numberError}
                            inputProps={{
                                min: 1,
                                step: 1,
                                pattern: "^[1-9][0-9]*$", // Только положительные целые числа
                            }}
                            disabled={readonlyForCurrentUser}
                        ></TextField>

                        <TextField
                            label="Sales"
                            type="number"
                            required
                            defaultValue={isNewBand ? undefined : chosenObject?.bestAlbum?.sales}
                            onChange={(e) => handleBestAlbumChange('sales', Number(e.target.value))}
                            error={!!errors.numberError}
                            helperText={errors.numberError}
                            inputProps={{
                                min: 1,
                                step: 1,
                                pattern: "^[1-9][0-9]*$", // Только положительные целые числа
                            }}
                            disabled={readonlyForCurrentUser}
                        ></TextField>
                    </Box>
                    }

                    <TextField
                        label="Albums count"
                        type="number"
                        required
                        defaultValue={isNewBand ? undefined : chosenObject?.albumsCount}
                        onChange={handleAlbumsCountChange}
                        error={!!errors.numberError}
                        helperText={errors.numberError}
                        inputProps={{
                            min: 1,
                            step: 1,
                            pattern: "^[1-9][0-9]*$", // Только положительные целые числа
                        }}
                        disabled={readonlyForCurrentUser}
                    ></TextField>

                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <DatePicker
                            label="Establishment date"
                            defaultValue={isNewBand ? null : dayjs(establishmentDate)}
                            onChange={handleEstablishmentDateChange}
                            //@ts-ignore
                            renderInput={(params) => <TextField {...params} />}
                            required
                        />
                    </LocalizationProvider>

                    <Stack direction="row" spacing={1} sx={{alignItems: 'center'}}>
                        <Typography>Create new studio</Typography>
                        <Switch
                            defaultValue={'false'}
                            checked={selectExistingStudios}
                            onChange={(e) => setSelectExistingStudios(e.target.checked)}
                            inputProps={{'aria-label': 'controlled'}}
                            disabled={readonlyForCurrentUser}
                        />
                        <Typography>Select existing studio</Typography>
                    </Stack>

                    {selectExistingStudios &&
                        <Select
                            defaultValue=""
                            variant="standard"
                            onChange={handleStudioChoosing}
                            disabled={readonlyForCurrentUser}
                            sx={{ marginBottom: '20px' }}
                        >
                            <MenuItem value="">
                                Не выбрано
                            </MenuItem>
                            {existingStudios.map((studio, index) => (
                                <MenuItem value={studio.id} key={index}>
                                    {studio.name}
                                </MenuItem>
                            ))}
                        </Select>
                    }

                    {!selectExistingStudios &&
                        <Box className="form-box-container">
                            <Typography sx={{marginBottom: '10px'}}>Studio</Typography>
                            <TextField
                                label="Name"

                                defaultValue={isNewBand ? undefined : chosenObject?.studio?.name}
                                onChange={(e) => handleStudioChange('name', e.target.value)}
                                disabled={readonlyForCurrentUser}
                            ></TextField>
                        </Box>
                    }


                    <Box sx={{display: "flex", justifyContent: "space-between"}}>
                        <Button onClick={handleClose} color="error" variant="outlined">
                            Cancel
                        </Button>
                        <Button
                            color="primary"
                            variant="contained"
                            type="submit"
                            disabled={readonlyForCurrentUser ||
                                !!errors.stringError ||
                                !!errors.numberError}>
                            {isNewBand ? 'Create' : 'Update'}
                        </Button>
                    </Box>


                </Box>


            </DialogContent>

            <Notification openCondition={requestError} onNotificationClose={handleNotificationClose} severity="error"
                          responseText="Error while fetching existing related objects"/>
        </Dialog>


    )
}

export default ObjectControlModal;