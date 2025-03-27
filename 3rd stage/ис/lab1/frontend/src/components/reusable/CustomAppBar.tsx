import React, {useEffect, useState} from "react";
import {AppBar, Badge, Button, IconButton, Toolbar, Typography} from "@mui/material";
import {clearUser} from "../../stores/userSlice.ts";
import LogoutIcon from "@mui/icons-material/Logout";
import {AccessRights} from "../../interfaces.ts";
import {useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../stores/store.ts";
import axiosInstance from "../../axiosConfig.ts";


const CustomAppBar: React.FC = () => {

    const [applicationsCount, setApplicationsCount] = useState<number>()

    const navigate = useNavigate()

    const dispatch = useDispatch<AppDispatch>();

    const user = useSelector((state: RootState) => state.user);

    const fetchApplications = () => {
        axiosInstance.get('api/admin/applications')
            .then((response) => {
                setApplicationsCount(response.data.length)
            })
    }

    const isAdmin = () => {
        return user.roles.includes(AccessRights.ADMIN)
    }

    useEffect(() => {
        fetchApplications()
        const intervalId = setInterval(fetchApplications, 1000)
        return () => clearInterval(intervalId)
    }, []);




    return (
        <AppBar position="static" >
            <Toolbar>
                <Button
                    key="Main screen"
                    onClick={() => {navigate('/main-screen')}}
                >
                    <Typography
                        variant="button"
                        component="div"
                        sx={{flexGrow: 1, color: 'white', textDecoration: 'underline'}}
                    >
                        Main screen
                    </Typography>
                </Button>
                {isAdmin() &&
                    <Badge badgeContent={applicationsCount} color="success">
                        <Button
                            key="Admin panel"
                            onClick={() => {navigate('/admin-panel')}}
                        >
                            <Typography
                                variant="button"
                                component="div"
                                sx={{flexGrow: 1, color: 'white', textDecoration: 'underline'}}
                            >
                                Admin panel
                            </Typography>
                        </Button>
                    </Badge>

                }


                <Typography variant="h6" component="div" sx={{flexGrow: 1, marginLeft: '27vw'}}>
                    Welcome, {isAdmin() ? 'admin' : 'user'} {user.username ? user.username : 'Unidentified jackal'}!
                </Typography>
                <IconButton
                    size="large"
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    title="Logout"
                    onClick={() => {
                        dispatch(clearUser())
                        localStorage.removeItem('accessToken')
                        navigate('/login')
                    }}
                >
                    <LogoutIcon/>
                </IconButton>
            </Toolbar>
        </AppBar>
    )
}

export default CustomAppBar;