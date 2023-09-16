<?php
session_start();

if (isset($_SESSION["batman0v"])){
    $_SESSION["batman0v"] = array();
}

include "add_in_table.php";