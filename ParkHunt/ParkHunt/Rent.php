<?php
    $con = mysqli_connect("localhost", "rohit", "mzJRCKP5uvAvPCeX", "Parkhunt");

    $rate = $_POST["rate"];
    $lat = $_POST["lat"];
    $lng = $_POST["lng"];
    $userID = $_POST["userid"];

    $rated = doubleval($rate);
    $latd = doubleval($lat);
    $lngd = doubleval($lng);
    $userId = intval($userID);
    
    $statement = mysqli_prepare($con, "INSERT INTO parking_lots (rate, lat, lng, UserId) VALUES (?, ?, ?, ?)");

    mysqli_stmt_bind_param($statement, "dddi", $rated, $latd, $lngd, $userId);
    mysqli_stmt_execute($statement);
    
	mysqli_stmt_close($statement);
	
	mysqli_close($con);
	
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>