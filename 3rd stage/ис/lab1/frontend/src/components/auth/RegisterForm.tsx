import React, {useState} from "react";
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    FormControlLabel,
    FormGroup,
    Snackbar,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import '../../styles/FormStyles.css'
import {useNavigate} from "react-router-dom";
import axiosInstance from "../../axiosConfig.ts";
import {AccessRights, RegisterData} from "../../interfaces.ts";
import {setUser} from "../../stores/userSlice.ts";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../../stores/store.ts";

const RegisterForm: React.FC = () => {
    const [username, setUsername] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [adminAccess, setAdminAccess] = useState(false)
    const [loading, setLoading] = useState(false);
    const [loginError, setLoginError] = useState(false);
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [errors, setErrors] = useState<{ password?: string; confirmPassword?: string }>({});
    const navigate = useNavigate()
    const dispatch = useDispatch<AppDispatch>();


    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newPassword = event.target.value;
        setPassword(newPassword);

        // Проверка совпадения с подтверждением
        if (confirmPassword && newPassword !== confirmPassword) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                confirmPassword: "Passwords don't match",
            }));
        } else {
            setErrors((prevErrors) => ({ ...prevErrors, confirmPassword: undefined }));
        }
    };

    const handleConfirmPasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const newConfirmPassword = event.target.value;
        setConfirmPassword(newConfirmPassword);

        // Проверка совпадения с паролем
        if (newConfirmPassword !== password) {
            setErrors((prevErrors) => ({
                ...prevErrors,
                confirmPassword: "Passwords don't match",
            }));
        } else {
            setErrors((prevErrors) => ({...prevErrors, confirmPassword: undefined}));
        }
    };

    const handleAdminAccessChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setAdminAccess(event.target.checked)
    }

    const handleLogin = async (event: React.FormEvent) => {
        event.preventDefault()
        setLoading(true)
        setLoginError(false)

        const registerData: RegisterData = {
            username: username,
            password: password,
            roles: [
                adminAccess ? AccessRights.ADMIN : AccessRights.USER
            ]
        }

        if (!errors.password && !errors.confirmPassword) {
            axiosInstance.post('api/auth/register', registerData)
                .then((response) => {
                    if (response.status === 201 || response.status === 200) {
                        localStorage.setItem('accessToken', response.data.jwt)
                        const fetchedUser = {
                            id: response.data.id,
                            roles: response.data.roles,
                            username: response.data.username
                        }
                        dispatch(setUser(fetchedUser))
                        navigate('/login')
                    }

                })
                .catch((error) => {
                    setLoginError(true)
                    console.log(error)
                })
                .finally(() => setLoading(false))
        }
    }

    const handleNotificationCLose = () => {
        setLoginError(false)
    }

    return (
        <div className="form-container">
            <Box
                className="base-form"
                component="form"
                onSubmit={handleLogin}
                sx={{display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2, width: '300px'}}
            >
                <Typography variant="h5">Registration</Typography>
                <TextField
                    label="Username"
                    variant="outlined"
                    autoComplete="username"
                    value={username}
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
                    value={password}
                    autoComplete="new-password"
                    onChange={handlePasswordChange}
                    error={!!errors.password}
                    helperText={errors.password}
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
                    label="Confirm password"
                    variant="outlined"
                    type="password"
                    value={confirmPassword}
                    autoComplete="new-password"
                    onChange={handleConfirmPasswordChange}
                    error={!!errors.confirmPassword}
                    helperText={errors.confirmPassword}
                    sx={{
                        '& label': {
                            color: '#A0AAB4',
                        },
                        '& .MuiInputBase-input': {
                            color: '#A0AAB4',
                        },
                    }}
                />

                <FormGroup>
                    <FormControlLabel control={
                        <Switch
                            checked={adminAccess}
                            onChange={handleAdminAccessChange}
                            inputProps={{ 'aria-label': 'controlled' }}
                        />
                    }
                                      label="Request admin access"/>
                </FormGroup>



                <Button variant="contained"
                        disabled={!!errors.password || !!errors.confirmPassword || (username.length <= 0) || (password.length <= 0) || (confirmPassword.length <= 0)}
                        type="submit">
                    {loading ? <CircularProgress size={24} /> : 'Register'}
                </Button>

                <p style={{margin: 0}}>Already have an account? <a className="redirect-link"
                                                                   onClick={() => navigate('/login')}>Login</a></p>
            </Box>

            <Snackbar
                open={loginError}
                autoHideDuration={4000}
                onClose={handleNotificationCLose}
                anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            >
                <Alert
                    onClose={handleNotificationCLose}
                    severity="error"
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    Error while registration! Try again
                </Alert>
            </Snackbar>
        </div>

    )
}

export default RegisterForm;