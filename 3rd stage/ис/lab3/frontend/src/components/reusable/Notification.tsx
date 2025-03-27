import React from "react";
import {Alert, IconButton, Snackbar} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";

interface NotificationProps {
    openCondition: boolean,
    severity: string;
    responseText: string;
    onNotificationClose: () => void;
}

const Notification: React.FC<NotificationProps> = ({openCondition, severity, responseText, onNotificationClose}) => {
    const handleNotificationClose = () => {
        onNotificationClose()
    }

    const action = (
        <React.Fragment>
            <IconButton
                size="small"
                aria-label="close"
                color="inherit"
                onClick={handleNotificationClose}
            >
                <CloseIcon fontSize="small" />
            </IconButton>
        </React.Fragment>
    );


    return (
        <Snackbar
            open={openCondition}
            autoHideDuration={6000}
            onClose={handleNotificationClose}
            action={action}
            anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        >
            <Alert
                onClose={handleNotificationClose}
                // @ts-ignore
                severity={severity}
                variant="filled"
                sx={{ width: '100%' }}
            >
                <span dangerouslySetInnerHTML={{__html: responseText}}/>
            </Alert>
        </Snackbar>
    )
}

export default Notification;