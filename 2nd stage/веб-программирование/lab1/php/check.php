<?php

function checkDataValid($x, $y, $r) {
    return checkDigitsAfterDot($x, $y, $r) &&
        in_array($x, array(-5, -4, -3, -2, -1, 0, 1, 2, 3), false) &&
        is_numeric($y) && $y >= -3 && $y <= 5 &&
        is_numeric($r) && $r >= 1 && $r <= 5;
}

function checkDigitsAfterDot($x, $y, $r) {
    if (is_numeric($x) && is_numeric($y) && is_numeric($r)){
        $xArray = explode(".", $x);
        $yArray = explode(".", $y);
        $rArray = explode(".", $r);
        if ((strlen($xArray[0]) <= 14) && (strlen($yArray[0]) <= 14) && (strlen($rArray[0]) <= 14)) return true;
    }
    return false;
}

function atArea($x, $y, $r) {
    return
        // r == 1
        ($x == -1) && ($r == 1) && ($y == 0) ||
        ($x == 0) && ($r == 1) && ($y >= -0.4) && ($y <= 0.4) ||
        ($x == 1) && ($r == 1) && ($y >= 0) ||
        // r == 2
        ($x == -1) && ($r == 2) && ($y == 0) ||
        ($x == 0) && ($r == 2) && ($y >= -0.4) && ($y <= 0.9) ||
        ($x == 1) && ($r == 2) && ($y >= -0.9) && ($y <= 0.8) ||
        ($x == 2) && ($r == 2) && ($y >= -0.4) && ($y <= 0.9) ||
        ($x == 3) && ($r == 2) && ($y == 0) ||
        // r == 3
        ($x == -3) && ($r == 3) && ($y == 0) ||
        ($x == -2) && ($r == 3) && ($y >= -0.6) && ($y <= 1.1) ||
        ($x == -1) && ($r == 3) && ($y >= -0.7) && ($y <= 0.4) ||
        ($x == 0) && ($r == 3) && ($y >= -1.3) && ($y <= 1.2) ||
        ($x == 1) && ($r == 3) && ($y >= -0.7) && ($y <= 0.4) ||
        ($x == 2) && ($r == 3) && ($y >= -0.6) && ($y <= 1.1) ||
        ($x == 3) && ($r == 3) && ($y == 0) ||
        // r == 4
        ($x == -4) && ($r == 4) && ($y == 0) ||
        ($x == -3) && ($r == 4) && ($y >= -1.5) && ($y <= 1.3) ||
        ($x == -2) && ($r == 4) && ($y >= -0.8) && ($y <= 1.7) ||
        ($x == -1) && ($r == 4) && ($y >= -0.8) && ($y <= 0.6) ||
        ($x == 0) && ($r == 4) && ($y >= -1.9) && ($y <= 1.6) ||
        ($x == 1) && ($r == 4) && ($y >= -0.8) && ($y <= 0.6) ||
        ($x == 2) && ($r == 4) && ($y >= -0.8) && ($y <= 1.7) ||
        ($x == 3) && ($r == 4) && ($y >= -1.5) && ($y <= 1.3) ||
        // r == 5
        ($x == -5) && ($r == 5) && ($y >= -0.3) && ($y <= 0.3) ||
        ($x == -4) && ($r == 5) && ($y >= -1.7) && ($y <= 1.5) ||
        ($x == -3) && ($r == 5) && ($y >= -0.9) && ($y <= 2) ||
        ($x == -2) && ($r == 5) && ($y >= -1.3) && ($y <= 0.9) ||
        ($x == -1) && ($r == 5) && ($y >= -1) && ($y <= 0.8) ||
        ($x == 0) && ($r == 5) && ($y >= -2.3) && ($y <= 2) ||
        ($x == 1) && ($r == 5) && ($y >= -1) && ($y <= 0.9) ||
        ($x == 2) && ($r == 5) && ($y >= -1.3) && ($y <= 0.9) ||
        ($x == 3) && ($r == 5) && ($y >= -0.9) && ($y <= 2);
}

session_start();
$start = microtime(true);
date_default_timezone_set('Europe/Moscow');

$x = isset($_GET["x"]) ? $_GET["x"] : null;
$y = isset($_GET["y"]) ? str_replace(",", ".", $_GET["y"]) : null;
$r = isset($_GET["r"]) ? $_GET["r"] : null;

if (!checkDataValid($x, $y, $r)) {
    http_response_code(400);
    return;
}

if(!isset($_SESSION["batman0v"])) {
    $_SESSION["batman0v"] = array();
}

$coordinatesAtArea = atArea($x, $y, $r);
$currentTime = date("H:i:s");
$time = number_format(microtime(true) - $start, 10, ".", "") * 1000000;
$result = array($x, $y, $r, $currentTime, $time, $coordinatesAtArea);
array_push($_SESSION["batman0v"], $result);

include "add_in_table.php";