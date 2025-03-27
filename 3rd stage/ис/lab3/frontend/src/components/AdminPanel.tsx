import React, {useEffect, useState} from "react";
import {Box, IconButton, List, ListItem, Stack, Typography} from "@mui/material";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import axiosInstance from "../axiosConfig.ts";
import Notification from "./reusable/Notification.tsx";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import CustomAppBar from "./reusable/CustomAppBar.tsx";
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import HotelIcon from '@mui/icons-material/Hotel';

interface Application {
    id: number;
    username: string
}

const AdminPanel: React.FC = () => {

    const [applications, setApplications] = useState<Application[]>()
    const [requestError, setRequestError] = useState<boolean>(false)
    const [operationSuccessful, setOperationSuccessful] = useState<boolean>(false)
    const [action, setAction] = useState<string>()

    useEffect(() => {
        axiosInstance.get('api/admin/applications')
            .then((response) => {
                setApplications(response.data)
            })
            .catch(() => setRequestError(true))
    }, []);

    const handleNotificationClose = () => {
        setRequestError(false)
        setOperationSuccessful(false)
    }

    const acceptApplication = (id: number) => {
        axiosInstance.post(`api/admin/applications/${id}/approve`)
            .then(() => {
                setApplications(applications?.filter(a => a.id !== id))
                setOperationSuccessful(true)
                setAction("accepted")

            })
            .catch(() => setRequestError(true))

    }

    const declineApplication = async (id: number) => {
        axiosInstance.post(`api/admin/applications/${id}/reject`)
            .then(() => {
                setApplications(applications?.filter(a => a.id !== id))
                setOperationSuccessful(true)
                setAction("declined")
            })
            .catch(() => setRequestError(true))
    }

    return (
        <Box sx={{flexGrow: 1, minWidth: '100vw'}}>
            <CustomAppBar/>
            <Typography sx={{textAlign: 'center', marginTop: '20px'}} variant="h3" component="div">
                Active applications for admin access
            </Typography>
                <Box sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: '#282828',
                    marginTop: '20px'
                }}>
                    {applications && applications.length > 0 &&
                    <List>
                        {applications.map((application) => (
                            <ListItem>
                                <Stack direction="row" spacing={1} sx={{alignItems: 'center'}}>
                                    <AccountCircleIcon fontSize="large"/>
                                    <Typography variant="h5" component="div">
                                        New application from user {application.username} (id: {application.id})
                                    </Typography>

                                    <div style={{alignItems: 'center', marginLeft: '50px'}}>

                                        <IconButton
                                            size="large"
                                            title="Accept"
                                            sx={{color: '#0A5F38'}}
                                            onClick={() => acceptApplication(application.id)}
                                        >
                                            <CheckCircleOutlineIcon/>
                                        </IconButton>

                                        <IconButton
                                            size="large"
                                            title="Decline"
                                            sx={{color: '#7B001C'}}
                                            onClick={() => declineApplication(application.id)}
                                        >
                                            <HighlightOffIcon/>
                                        </IconButton>
                                    </div>
                                </Stack>


                            </ListItem>
                        ))
                        }
                    </List>
                    }

                    {(!applications || applications.length === 0) &&
                        <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}>
                            <HotelIcon style={{width: '60px', height: '60px'}}/>
                            <Typography variant="h4">
                                No applications, just chill
                            </Typography>
                        </Box>
                    }
                </Box>




            <Notification openCondition={requestError} onNotificationClose={handleNotificationClose} severity="error"
                          responseText="Request error, try again"/>
            <Notification
                openCondition={operationSuccessful}
                onNotificationClose={handleNotificationClose}
                severity="success"
                responseText={`Application has been successfully ${action}`}
            />

        </Box>
    )
}

export default AdminPanel;