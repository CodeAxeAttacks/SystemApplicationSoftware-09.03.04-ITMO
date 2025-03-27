import React, {useState} from "react";
import {Box, Button, TextField, Typography} from "@mui/material";
import { useNavigate } from 'react-router-dom';
import '../../styles/FormStyles.css'
import axiosInstance from "../../axiosConfig.ts";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../../stores/store.ts";
import {setUser} from "../../stores/userSlice.ts";
import Notification from "../reusable/Notification.tsx";


const LoginForm: React.FC = () => {
    const [username, setUsername] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [error, setError] = useState(false)
    const navigate = useNavigate();

    const dispatch = useDispatch<AppDispatch>();

    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault()

        axiosInstance.post('api/auth/login', {
            username: username,
            password: password
        })
            .then((response) => {
                if (response.status === 200) {
                    localStorage.setItem('accessToken', response.data.jwt)
                    const fetchedUser = {
                        id: response.data.id,
                        roles: response.data.roles,
                        username: response.data.username
                    }
                    dispatch(setUser(fetchedUser))
                    navigate('/main-screen')
                }
            })
            .catch((error) => {
                setError(true)
                console.log(error)
            })

    }
    const handleNotificationCLose = () => {
        setError(false)
    }


    return (
        <div className="form-container">
            <Box
                className="base-form"
                component="form"
                onSubmit={handleLogin}
                sx={{ display: 'flex', alignItems: 'center', flexDirection: 'column', gap: 2, width: '300px'}}
            >
                <Typography variant="h5">Authorization</Typography>
                <TextField
                    label="Usename"
                    variant="outlined"
                    value={username}
                    autoComplete="username"
                    onChange={(e) => setUsername(e.target.value)}
                    sx={{
                        '& label': {
                            color: '#A0AAB4',
                        },
                        '& .MuiInputBase-input': {
                            color: '#A0AAB4',
                        },
                    }}
                />
                <TextField
                    label="Password"
                    variant="outlined"
                    type="password"
                    autoComplete="current-password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    sx={{
                        '& label': {
                            color: '#A0AAB4',
                        },
                        '& .MuiInputBase-input': {
                            color: '#A0AAB4',
                        },
                    }}
                />

                <Button variant="contained" disabled={(username.length <= 0) || (password.length <= 0)} type="submit">Login</Button>

                <p style={{margin: 0}}>Don't have an account? <a className="redirect-link" onClick={() => navigate('/register')}>Register</a></p>

            </Box>

            <Notification
                openCondition={error}
                onNotificationClose={handleNotificationCLose}
                severity="error"
                responseText="Error with login! Check credentials and try again"
                />

        </div>

    )
}

export default LoginForm;