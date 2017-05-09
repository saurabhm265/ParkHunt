<?php
    
    date_default_timezone_set("America/Chicago");
    $con = mysqli_connect("localhost", "rohit", "mzJRCKP5uvAvPCeX", "Parkhunt");
    
    $userID = $_REQUEST["userid"];
    $parkingLotID = $_REQUEST["lotid"];
    $hours = $_REQUEST["hours"];
    $rate =$_REQUEST["rate"];
   
    //parkhunt.xyz/booking.php?userid=12&parkingLotID=4&hours=4&rate=7.00
    $userIDInt = intval($userID);
    $hoursInt = intval($hours);
    $lotIDInt=intval($parkingLotID);
    $rateDb=doubleval($rate);
    
    //$createdTime = $_REQUEST["createdTime"];
    //$expireTime = $_REQUEST["expireTime"];
    // get All times in SQL Format 
    $createdTime = date("Y-m-d H:i:s", strtotime("now")); 
    $expireTime = date("Y-m-d H:i:s", strtotime("+".$hoursInt." hours"));
    
    $sql = "INSERT INTO `Parkhunt`.`Booking` (`BookingID`, `UserID`, `ParkingLotID`, `CreatedTime`, `ExpireTime`, `Rate`) VALUES (NULL, \'12\', \'4\', CURRENT_TIME(), CURRENT_TIME(), \'7.00\');";

    $statement = mysqli_prepare($con, "INSERT INTO Booking (UserID,ParkingLotID,CreatedTime,ExpireTime,Rate) VALUES (?, ?, ?, ?, ?)");

    mysqli_stmt_bind_param($statement, "iissd", $userIDInt,$lotIDInt,$createdTime,$expireTime,$rateDb);
    mysqli_stmt_execute($statement);
	
	
    mysqli_stmt_close($statement);
	
	mysqli_close($con);
	
    $response = array();
    $response["success"] = true; 
    $response["all"]            = $_REQUEST;
    $response["userID"]         = $_REQUEST["userid"];
    $response["parkingLotID"]    = $_REQUEST["lotid"];
    $response["hours"]           = $_REQUEST["hours"];
    $response["rate"]             = $_REQUEST["rate"];

    echo json_encode($response);
?>