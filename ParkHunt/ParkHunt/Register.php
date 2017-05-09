<?php
    $con = mysqli_connect("localhost", "rohit", "mzJRCKP5uvAvPCeX", "Parkhunt");
    
    $name = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $userpoints =$_POST["userpoints"];

    $intUserPoints = intval($userpoints);

    $statement = mysqli_prepare($con, "INSERT INTO users (name, email, password,userpoints) VALUES (?, ?, ?,?)");

    mysqli_stmt_bind_param($statement, "sssi", $name, $email, $password,$intUserPoints);
    mysqli_stmt_execute($statement);
    
	mysqli_stmt_close($statement);
	
	mysqli_close($con);
	
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>