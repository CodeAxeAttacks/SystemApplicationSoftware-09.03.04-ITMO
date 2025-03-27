import React, {useEffect, useState} from 'react';
import {DataGrid, GridColDef, GridEventListener} from '@mui/x-data-grid';
import DeleteIcon from '@mui/icons-material/Delete';
import {
    Button,
    CircularProgress,
} from '@mui/material';
import {enUS} from '@mui/x-data-grid/locales';
import {
    AccessRights, BestAlbum,
    Coordinates, Genre,
    // importHistoryData,
    RowData, Studio,
} from "../interfaces.ts";
import axiosInstance from "../axiosConfig.ts";
import ObjectControlModal from "./reusable/ObjectControlModal.tsx";
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
// import FileDownloadIcon from '@mui/icons-material/FileDownload';
// import HistoryIcon from '@mui/icons-material/History';
// import CloseIcon from '@mui/icons-material/Close';
import {useSelector} from "react-redux";
import {RootState} from "../stores/store.ts";
import Notification from "./reusable/Notification.tsx";


const CollectionObjectsDataGrid: React.FC = () => {
    const [rows, setRows] = useState<RowData[]>([]);
    const [loading, setLoading] = useState(true)
    const [selectedRowIds, setSelectedRowIds] = useState<number[]>([]);
    const [open, setOpen] = useState(false)
    const [chosenObject, setChosenObject] = useState<RowData>()
    const [isNewBand, setIsNewBand] = useState<boolean>(false)
    const [requestError, setRequestError] = useState<boolean>(false)
    // const [importFileActive, setImportFileActive] = useState<boolean>(false)
    // const [selectedFile, setSelectedFile] = useState<File | null>(null)
    // const [importInProgress, setImportInProgress] = useState<boolean>(false)
    // const [openImportHistory, setOpenImportHistory] = useState<boolean>(false)
    // const [fileImportStatus, setFileImportStatus] = useState<string>('')

    // const [importHistoryData, setImportHistoryData] = useState<importHistoryData[]>()

    const user = useSelector((state: RootState) => state.user);


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


                setRows(rowData)
                setLoading(false)
            })
    }

    const isAdmin = () => {
        return user.roles.includes(AccessRights.ADMIN)
    }

    // const fetchImportHistoryData = () => {
    //     const url = isAdmin() ? 'api/import-history' : 'api/import-history/my';
    //
    //     axiosInstance.get(url)
    //         .then(response => setImportHistoryData(response.data))
    //         .catch(() => setRequestError(true));
    // }

    useEffect(() => {
        fetchGroups()
        // fetchImportHistoryData()
        const intervalId = setInterval(fetchGroups, 1000)

        return () => clearInterval(intervalId)
    }, [])

    if (loading) {
        return <div><CircularProgress size={24} sx={{marginTop: 2, marginBottom: 2}} /></div>;
    }

    const createObject = () => {
        setIsNewBand(true)
        setOpen(true)
    }

    // const openImportModal = () => {
    //     setImportFileActive(true)
    // }
    //
    // const openImportHistoryModal = () => {
    //     fetchImportHistoryData()
    //     setOpenImportHistory(true)
    // }

    const handleClickOpen: GridEventListener<'rowClick'> = (params) => {
        setChosenObject(params.row);
        setIsNewBand(false)
        setOpen(true)
    };

    const handleClose = () => {
        setOpen(false);
        setIsNewBand(false)
    };


    // const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    //     const file = event.target.files?.[0];
    //     if (file) {
    //         if (file.type !== "application/json") {
    //             alert("Please choose a json file");
    //             return;
    //         }
    //         setSelectedFile(file);
    //     }
    // };
    //
    // const handleFileUpload = async () => {
    //     if (!selectedFile) {
    //         console.log("No file selected.");
    //         return;
    //     }
    //
    //     const formData = new FormData();
    //     formData.append("file", selectedFile);
    //
    //     // Отправка файла через axios или любой другой механизм
    //     setImportInProgress(true)
    //
    //     await axiosInstance.post("/api/import", formData, {
    //         headers: {
    //             "Content-Type": "multipart/form-data",
    //         },
    //     })
    //         .then(response => {
    //             if (response.status === 200) setFileImportStatus('success')
    //         })
    //         .catch(() => {
    //             setFileImportStatus('error')
    //         })
    //         .finally(() => {
    //             setImportInProgress(false)
    //             closeFileImport()
    //         })
    // };
    //
    // const closeFileImport = () => {
    //     setImportFileActive(false)
    //     setSelectedFile(null)
    // }

    // const closeImportHistory = () => {
    //     setOpenImportHistory(false)
    // }

    const handleSelectionChange = (newSelection: any) => {
        setSelectedRowIds(newSelection);
    };

    const handleDelete = () => {
        selectedRowIds.forEach(rowId => {
            axiosInstance.delete(`api/music-bands/${rowId}`)
                .then(() => {
                    // Удаляем выбранные строки
                    setRows((prevRows) => prevRows.filter((row) => !selectedRowIds.includes(row.id) || !checkEditRights(row)));
                    // Очищаем выбранные строки после удаления
                    setSelectedRowIds([]);
                })
                .catch(() => {
                    setRequestError(true)
                })
        })
    };

    const handleRequestError = () => {
        setRequestError(true)
    }

    const handleNotificationClose = () => {
        setRequestError(false)
    }


    const checkEditRights = (item: RowData) => {
        return isAdmin() || (item.createdBy === user.id)
    }

    // const downloadFile = (fileName: string) => {
    //     axiosInstance.get(`api/import/download/${fileName}`, { responseType: 'blob' })
    //         .then(response => {
    //             const url = window.URL.createObjectURL(new Blob([response.data]));
    //
    //             const link = document.createElement('a');
    //             link.href = url;
    //             link.setAttribute('download', fileName);
    //
    //             document.body.appendChild(link);
    //             link.click();
    //
    //             document.body.removeChild(link);
    //             window.URL.revokeObjectURL(url);
    //         })
    //         .catch(error => {
    //             console.error("Error downloading file: ", error);
    //         });
    // };


    const columns: GridColDef[] = [
        { field: 'id', headerName: 'ID', width: 90},
        { field: 'name', headerName: 'Name', width: 150},
        { field: 'coordinates', headerName: 'Coordinates', width: 150, renderCell: (params) => {
                return `(${params.value.x}, ${params.value.y})`
            }},
        { field: 'creationDate', headerName: 'Creation Date', width: 150, renderCell: (params) => {
                return `${params.value.getDate().toString().padStart(2, '0')}.${params.value.getMonth() + 1}.${params.value.getFullYear()}`
            }},
        { field: 'genre', headerName: 'Genre', width: 100},
        { field: 'numberOfParticipants', headerName: 'Participants', width: 95 },
        { field: 'singlesCount', headerName: 'Singles', width: 95},
        { field: 'description', headerName: 'Description', width: 200},
        { field: 'bestAlbum', headerName: 'Best album', width: 150, renderCell: (params) => {
                return params.value !== null ? params.value.name : null
            }},
        { field: 'albumsCount', headerName: 'Albums', width: 95},
        { field: 'establishmentDate', headerName: 'Establishment date', width: 110, renderCell: (params) => {
                return `${params.value.getDate().toString().padStart(2, '0')}.${params.value.getMonth() + 1}.${params.value.getFullYear()}`
            }},
        { field: 'studio', headerName: 'Studio', width: 110, renderCell: (params) => {
                return params.value !== null ? params.value.name : null
            }},

        { field: 'createdBy', headerName: 'createdBy', width: 90, renderCell: (params) => {
            return params.value === user.id ? `${params.value} (You)` : params.value
            }}
    ];


    return (
        <div style={{height: 700, width: '100%'}}>
            <div style={{display: 'flex', flexDirection: 'row', margin: '10px 5px', justifyContent: "space-between"}}>
                <div style={{display: 'flex', flexDirection: 'row', gap: '10px'}}>
                    <Button onClick={createObject} variant="contained">
                        <AddCircleOutlineIcon sx={{marginRight: 1}}/>
                        Add band
                    </Button>
                    {/*<Button onClick={openImportModal} variant="contained">*/}
                    {/*    <FileDownloadIcon sx={{marginRight: 1}}/>*/}
                    {/*    Import from file*/}
                    {/*</Button>*/}
                </div>

                {/*<div style={{display: "flex", justifyContent: "right", flexGrow: 1, marginRight: "5px"}}>*/}
                {/*    <Button onClick={openImportHistoryModal} variant="contained">*/}
                {/*        <HistoryIcon sx={{marginRight: 1}}/>*/}
                {/*       Show import history*/}
                {/*    </Button>*/}
                {/*</div>*/}

                <div>
                    {selectedRowIds.length !== 0 &&
                        <><Button
                            variant="outlined"
                            color="primary"
                            onClick={handleDelete}
                            disabled={selectedRowIds.length === 0}
                            title="Delete bands"
                        >
                            <DeleteIcon/>
                        </Button>

                        </>
                    }

                </div>
            </div>


            <DataGrid
                rows={rows}
                columns={columns}
                checkboxSelection
                disableRowSelectionOnClick
                onRowSelectionModelChange={handleSelectionChange}
                onRowClick={handleClickOpen}
                localeText={enUS.components.MuiDataGrid.defaultProps.localeText}
            />

            {isNewBand
                ? <ObjectControlModal
                    modalOpen={open}
                    onModalCLose={handleClose}
                    chosenObject={chosenObject}
                    isNewBand={isNewBand}
                    onSendError={handleRequestError}
                    readonlyForCurrentUser={false}
                />
                : chosenObject &&
                <ObjectControlModal
                    modalOpen={open}
                    onModalCLose={handleClose}
                    chosenObject={chosenObject}
                    isNewBand={isNewBand}
                    onSendError={handleRequestError}
                    readonlyForCurrentUser={!checkEditRights(chosenObject)}
                />
            }

            {/*<Dialog open={importFileActive} onClose={closeFileImport} sx={{ '& .MuiDialog-paper': { width: '360px', maxWidth: 'none' } }}>*/}
            {/*    <DialogTitle>Import groups from file</DialogTitle>*/}
            {/*    <DialogContent>*/}
            {/*        {importInProgress ?*/}
            {/*            <div style={{display: "flex", flexDirection: "column", alignItems: "center", gap: "10px", justifyContent: "center"}}>*/}
            {/*                <CircularProgress size={40}/>*/}
            {/*                <Typography>*/}
            {/*                    Import in progress...*/}
            {/*                </Typography>*/}
            {/*            </div>*/}

            {/*            :*/}
            {/*            <div style={{*/}
            {/*                display: "flex",*/}
            {/*                flexDirection: "row",*/}
            {/*                alignItems: "center",*/}
            {/*                justifyContent: "center",*/}
            {/*                gap: "20px"*/}
            {/*            }}>*/}
            {/*                <Button*/}
            {/*                    variant="contained"*/}
            {/*                    component="label"*/}
            {/*                >*/}
            {/*                    Select file*/}
            {/*                    <input*/}
            {/*                        type="file"*/}
            {/*                        hidden*/}
            {/*                        onChange={handleFileChange}*/}
            {/*                        accept=".json"*/}
            {/*                    />*/}
            {/*                </Button>*/}

            {/*                {selectedFile ? <p>Selected file: {selectedFile.name}</p> : "No file selected"}*/}
            {/*            </div>*/}

            {/*        }*/}


            {/*    </DialogContent>*/}

            {/*    <DialogActions>*/}
            {/*        <Button*/}
            {/*            variant="outlined"*/}
            {/*            color="error"*/}
            {/*            onClick={closeFileImport}*/}
            {/*        >Cancel</Button>*/}

            {/*        <Button*/}
            {/*            variant="contained"*/}
            {/*            color="primary"*/}
            {/*            onClick={handleFileUpload}*/}
            {/*            disabled={!selectedFile}*/}
            {/*        >*/}
            {/*            Start import*/}
            {/*        </Button>*/}
            {/*    </DialogActions>*/}

            {/*</Dialog>*/}

            {/*<Dialog open={openImportHistory} onClose={closeImportHistory} sx={{ '& .MuiDialog-paper': { width: '850px', maxWidth: 'none' } }}>*/}
            {/*    <DialogTitle>*/}
            {/*        <div style={{ display: "flex", flexDirection: "row", justifyContent: "space-between", alignItems: "center"}}>*/}
            {/*            Import history*/}
            {/*            <IconButton onClick={closeImportHistory}>*/}
            {/*                <CloseIcon/>*/}
            {/*            </IconButton>*/}
            {/*        </div>*/}

            {/*    </DialogTitle>*/}
            {/*    <DialogContent>*/}
            {/*        <TableContainer component={Paper}>*/}
            {/*            <Table sx={{ minWidth: 750 }} aria-label="simple table">*/}
            {/*                <TableHead>*/}
            {/*                    <TableRow>*/}
            {/*                        <TableCell>Operation id</TableCell>*/}
            {/*                        <TableCell align="center">Status</TableCell>*/}
            {/*                        <TableCell align="center">Operation starter</TableCell>*/}
            {/*                        <TableCell align="center">Objects added</TableCell>*/}
            {/*                        <TableCell align="center">File</TableCell>*/}
            {/*                    </TableRow>*/}
            {/*                </TableHead>*/}
            {/*                <TableBody>*/}
            {/*                    {importHistoryData && importHistoryData*/}
            {/*                        .filter((row) => isAdmin() || row.userId === user.id)*/}
            {/*                        .map((row) => (*/}
            {/*                        <TableRow*/}
            {/*                            key={row.id}*/}
            {/*                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}*/}
            {/*                        >*/}
            {/*                            <TableCell component="th" scope="row">*/}
            {/*                                {row.id}*/}
            {/*                            </TableCell>*/}
            {/*                            <TableCell align="center">*/}
            {/*                                <p style={{margin: "0", color: row.status === 'SUCCESS' ? "green" : "#C10020"}}>{row.status}</p>*/}
            {/*                            </TableCell>*/}
            {/*                            <TableCell align="center">{row.userId}</TableCell>*/}
            {/*                            <TableCell align="center">{row.objectsCount}</TableCell>*/}
            {/*                            <TableCell align="center">*/}
            {/*                                {row.status === 'SUCCESS' &&*/}
            {/*                                    <IconButton*/}
            {/*                                        onClick={() => {downloadFile(row.fileName)}}*/}
            {/*                                    >*/}
            {/*                                        <FileDownloadIcon/>*/}
            {/*                                    </IconButton>*/}
            {/*                                }*/}
            {/*                            </TableCell>*/}
            {/*                        </TableRow>*/}
            {/*                    ))}*/}

            {/*                </TableBody>*/}
            {/*            </Table>*/}
            {/*        </TableContainer>*/}

            {/*        {importHistoryData && importHistoryData.filter((row) => isAdmin() || row.userId === user.id).length === 0 &&*/}
            {/*            <Typography color="#A9A9A9" fontSize="25px" fontStyle="italic" sx={{display: "flex", justifyContent: "center", marginTop: "10px"}}>*/}
            {/*                Nothing to show*/}
            {/*            </Typography>*/}
            {/*        }*/}

            {/*    </DialogContent>*/}

            {/*</Dialog>*/}


            <Notification openCondition={requestError} onNotificationClose={handleNotificationClose} severity="error" responseText="Operation on object failed, try again"/>

            {/*<Notification*/}
            {/*    openCondition={fileImportStatus !== ''}*/}
            {/*    onNotificationClose={() => {setFileImportStatus('')}}*/}
            {/*    severity={fileImportStatus}*/}
            {/*    responseText={fileImportStatus === 'success' ? 'File has been successfully imported' : 'Error while importing file'}*/}
            {/*    />*/}

        </div>
    );
};

export default CollectionObjectsDataGrid;
