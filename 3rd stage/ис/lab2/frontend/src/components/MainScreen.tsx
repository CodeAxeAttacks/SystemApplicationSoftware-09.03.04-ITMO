import React, {useEffect, useState} from "react";
import CollectionObjectsDataGrid from "./ObjectsTable.tsx";
import {
    Box,
    Button,
    ButtonGroup,
    Dialog, DialogActions, DialogContent,
    DialogTitle, InputLabel, MenuItem, Select, SelectChangeEvent,
    TextField,
} from "@mui/material";
import {ThemeProvider, createTheme} from '@mui/material/styles';
import LibraryMusicIcon from '@mui/icons-material/LibraryMusic';
import DateRangeIcon from '@mui/icons-material/DateRange';
import AlbumIcon from '@mui/icons-material/Album';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents';
import axiosInstance from "../axiosConfig.ts";
import Notification from "./reusable/Notification.tsx";
import CustomAppBar from "./reusable/CustomAppBar.tsx";
import {BestAlbum, Coordinates, Genre, RowData, Studio} from "../interfaces.ts";

const darkTheme = createTheme({
    palette: {
        mode: 'dark',
    },
});



const MainScreen: React.FC = () => {
    //настройки параметров в модальном окне ввода значений
    const [intModalValue, setIntModalValue] = useState<string>('');
    const [validationError, setValidationError] = useState(true);
    const [successRequest, setSuccessRequest] = useState(false)
    const [responseText, setResponseText] = useState<string>('')
    const [helperText, setHelperText] = useState('');
    const [isInformationalMessage, setIsInformationalMessage] = useState(false)
    const [requestError, setRequestError] = useState(false)

    const [bands, setBands] = useState<RowData[]>([]);
    const [selectedBandId, setSelectedBandId] = useState<number | undefined>()

    const [openIndex, setOpenIndex] = useState<number>()


    const defaultResponseErrorMessage = 'Request error, something went wrong...(('

    const fetchGroups = () => {
        axiosInstance.get('api/music-bands', {
        })
            .then((response) => {
                const rowData: RowData[] = response.data.map((item: any) => ({
                    id: item.id,
                    name: item.name,
                    coordinates: item.coordinates as Coordinates,
                    creationDate: new Date(item.creationDate), // преобразование строки в объект Date
                    genre: item.genre as Genre,
                    numberOfParticipants: item.numberOfParticipants,
                    singlesCount: item.singlesCount,
                    description: item.description,
                    bestAlbum: item.bestAlbum as BestAlbum,
                    albumsCount: item.albumsCount, // Приведение к типу Semester
                    establishmentDate: new Date(item.establishmentDate),
                    studio: item.studio as Studio,
                    createdBy: item.createdBy,
                }));

                setBands(rowData)
            })
    }

    useEffect(() => {
        fetchGroups()
        const intervalId = setInterval(fetchGroups, 1000)
        return () => clearInterval(intervalId)
    }, []);

    const handleSpecialButtonClick = (index: number) => {
        setOpenIndex(index)
    }


    const handleSpecialActionButtonClick = (actionIndex: number) => {
        switch (actionIndex) {
            case 0:
                axiosInstance.get(`api/special/count-by-studio`, {params: {
                    studioId: intModalValue
                    }})
                    .then((response) => {
                        if (response.status === 200) {
                            setSuccessRequest(true)
                            setResponseText(`Number of bands with studio id ${intModalValue} is ${response.data}`)
                            setIsInformationalMessage(true)
                        }
                    })
                    .catch(() => {
                        setRequestError(true)
                        setResponseText(defaultResponseErrorMessage)
                    })
                setOpenIndex(-1)
                setIntModalValue('')
                setValidationError(true)
                break
            case 2:
                axiosInstance.get(`api/special/albums-count-greater`, {params: {
                        albumsCount: intModalValue
                    }})
                    .then((response) => {
                        if (response.status === 200) {
                            const matchesArray = response.data.length !== 0
                                ? response.data
                                    .map((item: { id: any; name: any; }) => `ID: ${item.id}. Name: ${item.name} <br>`)
                                    .join('')
                                : 'No matches'
                            setSuccessRequest(true)
                            setResponseText(matchesArray)
                            setIsInformationalMessage(true)
                        }
                    })
                    .catch(() => {
                        setRequestError(true)
                        setResponseText(defaultResponseErrorMessage)
                    })
                setOpenIndex(-1)
                setIntModalValue('')
                setValidationError(true)
                break

            case 3:
                axiosInstance.put(`api/special/add-single`, null, {params: {
                    id: selectedBandId,
                }})
                    .then((response) => {
                        if (response.status === 200) {
                            setSuccessRequest(true)
                            setResponseText('Single has been successfully added!')
                            setIsInformationalMessage(false)
                        }
                    })
                    .catch(() => {
                        setRequestError(true)
                        setResponseText(defaultResponseErrorMessage)
                    })
                setSelectedBandId(undefined)
                setOpenIndex(-1)
                break

            case 4:
                axiosInstance.post(`api/special/nominate`, null, {params: {
                        id: selectedBandId
                    }
                })
                    .then((response) => {
                        if (response.status === 200) {
                            setSuccessRequest(true)
                            setResponseText('Band has been successfully nominated!')
                            setIsInformationalMessage(false)
                        }
                    })
                    .catch(() => {
                        setRequestError(true)
                        setResponseText(defaultResponseErrorMessage)
                    })
                setOpenIndex(-1)
                setSelectedBandId(undefined)
                break

            default:
                setOpenIndex(0)
        }
    }

    const handleNotificationCLose = () => {
        setSuccessRequest(false)
        setRequestError(false)
    }

    const getMaxEstablishmentDate = () => {
        axiosInstance.get(`api/special/max-establishment-date`)
            .then((response) => {
                if (response.status === 200) {
                    setSuccessRequest(true)
                    setResponseText(`Found object: id = ${response.data.id}, name = ${response.data.name}`)
                    setIsInformationalMessage(true)
                }
            })
            .catch(() => {
                setRequestError(true)
                setResponseText(defaultResponseErrorMessage)
            })
    }

    const handleBandSelection = (event: SelectChangeEvent) => {
        setSelectedBandId(Number(event.target.value));
    };


    const validateIntModalValue = (value: string) => {
        if (parseInt(value) < 0 || !Number.isInteger(+value) || value.length === 0) {
            setValidationError(true);
            setHelperText('Value must be a positive integer!');
        } else {
            setValidationError(false);
            setHelperText('');
        }
    };

    // Обработка изменения значения в поле
    const handleIntModalValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const inputValue = event.target.value;
        setIntModalValue(inputValue);
        validateIntModalValue(inputValue); // Валидируем при каждом изменении
    };


    const buttons = [
        <Button key="0" onClick={() => handleSpecialButtonClick(0)}><LibraryMusicIcon sx={{marginRight: 1}}/> Count by studio </Button>,
        <Button key="1" onClick={getMaxEstablishmentDate}> <DateRangeIcon sx={{marginRight: 1}}/> Find with max establishment date</Button>,
        <Button key="2" onClick={() => handleSpecialButtonClick(2)}> <AlbumIcon sx={{marginRight: 1}}/>Albums count greater </Button>,
        <Button key="3" onClick={() => handleSpecialButtonClick(3)}><AddCircleOutlineIcon sx={{marginRight: 1}}/>Add single</Button>,
        <Button key="4" onClick={() => handleSpecialButtonClick(4)}><EmojiEventsIcon sx={{marginRight: 1}}/>Nominate band for a prize</Button>,
    ];

    return (
                <Box sx={{flexGrow: 1, minWidth: '100vw'}}>
                    <CustomAppBar/>

                    <ThemeProvider theme={darkTheme}>

                        <CollectionObjectsDataGrid/>

                <ButtonGroup size="large" aria-label="Large button group" sx={{marginTop: 6, width: '90%'}}>
                    {buttons}
                </ButtonGroup>


                    <Notification
                        onNotificationClose={handleNotificationCLose}
                        openCondition={successRequest}
                        responseText={responseText}
                        severity={isInformationalMessage ? 'info' : 'success'}
                    />

                        <Dialog open={openIndex === 0} onClose={() => setOpenIndex(-1)}>
                            <DialogTitle>Count bands with studio value = </DialogTitle>
                            <DialogContent>
                                <TextField
                                    required
                                    label="Studio id value"
                                    type="number"
                                    value={intModalValue}
                                    onChange={handleIntModalValueChange}
                                    error={validationError}
                                    helperText={helperText}
                                    sx={{marginTop: 2, width: '100%'}}
                                ></TextField>
                            </DialogContent>

                            <DialogActions>
                                <Button
                                    disabled={validationError}
                                    onClick={() => handleSpecialActionButtonClick(0)}
                                >
                                    Count
                                </Button>
                            </DialogActions>

                        </Dialog>

                        <Dialog open={openIndex === 2} onClose={() => {setOpenIndex(-1)}}>
                            <DialogTitle>Count bands with number of albums &gt; </DialogTitle>
                            <DialogContent>
                                <TextField
                                    required
                                    label="Albums count"
                                    type="number"
                                    value={intModalValue}
                                    onChange={handleIntModalValueChange}
                                    error={validationError}
                                    helperText={helperText}
                                    sx={{marginTop: 2, width: '100%'}}
                                ></TextField>
                            </DialogContent>

                            <DialogActions>
                                <Button
                                    disabled={validationError}
                                    onClick={() => handleSpecialActionButtonClick(2)}
                                >
                                    Search
                                </Button>
                            </DialogActions>

                        </Dialog>

                        <Dialog open={openIndex === 3} onClose={() => setOpenIndex(-1)}>
                            <DialogTitle>Add single to a selected band</DialogTitle>
                            <DialogContent>
                                <InputLabel>Band</InputLabel>
                                <Select
                                    sx={{width: '100%'}}
                                    defaultValue={undefined}
                                    label="Band"
                                    variant="standard"
                                    required
                                    onChange={handleBandSelection}
                                >
                                    {Object.values(bands).map((band, index) => (
                                        <MenuItem value={band.id} key={index}>
                                            {band.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </DialogContent>

                            <DialogActions>
                                <Button
                                    disabled={!selectedBandId}
                                    onClick={() => handleSpecialActionButtonClick(3)}
                                >
                                    Add single
                                </Button>
                            </DialogActions>

                        </Dialog>

                        <Dialog open={openIndex === 4} onClose={() => setOpenIndex(-1)}>
                            <DialogTitle>Nominate selected band for a prize</DialogTitle>
                            <DialogContent>
                                <InputLabel>Band</InputLabel>
                                <Select
                                    sx={{width: '100%'}}
                                    defaultValue={undefined}
                                    label="Band"
                                    variant="standard"
                                    required
                                    onChange={handleBandSelection}
                                >
                                    {Object.values(bands).map((band, index) => (
                                        <MenuItem value={band.id} key={index}>
                                            {band.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </DialogContent>

                            <DialogActions>
                                <Button
                                    disabled={!selectedBandId}
                                    onClick={() => handleSpecialActionButtonClick(4)}
                                >
                                   Nominate
                                </Button>
                            </DialogActions>

                        </Dialog>


                        <Notification
                            onNotificationClose={handleNotificationCLose}
                            openCondition={requestError}
                            responseText={responseText}
                            severity="error"
                        />
                </ThemeProvider>


</Box>


    );
}

export default MainScreen