<?php
	require 'core.inc.php';
	require 'connect.inc.php';

	$user_id = $_POST['user_id'];

	$query = "
		SELECT topic_name, score
		FROM
		(
			SELECT topic_id, score
			FROM scores_in
			WHERE user_id = $user_id
		) AS table1
		INNER JOIN
		(
			SELECT topic_id, topic_name
			FROM topics
		) AS table2
		ON table1.topic_id = table2.topic_id
	";

	($query_run = mysql_query($query)) or die(mysql_error());

	$res = array();

	while( ($row = mysql_fetch_assoc($query_run)) )
		$res[] = $row;
	
	echo json_encode($res);
?>