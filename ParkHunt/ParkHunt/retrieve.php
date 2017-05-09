<?php

require 'Update.php';

const DB_NAME="Parkhunt";
const DB_USER="rohit";
const DB_PASS="mzJRCKP5uvAvPCeX";
const DB_HOST="localhost";

$markers = array();
$sql = "select ID, UserId, rate, lat,lng, IsBooked from parking_lots";
$mysqli = new mysqli(DB_HOST, DB_USER, DB_PASS, DB_NAME);

if($res = $mysqli->query($sql)){
	while($row=$res->fetch_assoc()){
                $ID = $row['ID'];
                $UserId = $row['UserId'];
                $rate = $row['rate'];
                $lat = $row['lat'];
                $lng = $row['lng'];
                $IsBooked = $row['IsBooked'];
                $data= array(
                    "ID"=>$ID,
                    "UserId"=>$UserId,
                    "rate"=>$rate,
                    "lat"=>$lat,
                    "lng"=>$lng,
                    "IsBooked"=>$IsBooked
                );
                $marker[] = $data;
	}

        $markers = array("markers"=>$marker);

        echo json_encode($markers);
}

?>