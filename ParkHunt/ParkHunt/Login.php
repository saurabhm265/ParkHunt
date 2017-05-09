<?php
    $con = mysqli_connect("localhost", "rohit", "mzJRCKP5uvAvPCeX", "Parkhunt");
    
    $email = $_POST["email"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM users WHERE email = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $email,$password);
    mysqli_stmt_execute($statement);
	
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $ID, $name, $email, $password,$userpoints);
    
	
	
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement))
	{
            $response["success"] = true;  
            $response["userid"] = $ID; 
            $response["name"] = $name;
			$response["email"] = $email;
			$response["password"] = $password;
            $response["userpoints"] = $userpoints;
    }
    echo json_encode($response);
	
	mysqli_stmt_close($statement);
	mysqli_close($con);
?> 