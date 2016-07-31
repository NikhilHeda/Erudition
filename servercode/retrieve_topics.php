<?php
	require 'core.inc.php';
	require 'connect.inc.php';

	$user_id = $_POST['user_id'];

	if ($_POST['flag'] == 1){
		$query = "
				SELECT topic_name, topic_id
				FROM topics
			";
	}else if ($_POST['flag'] == 2){
		$query = "
			SELECT T.topic_name, T.topic_id, S.score
			FROM topics T, scores_in S
			WHERE T.topic_id = S.topic_id
			AND S.user_id = $user_id
		";
	}else if ($_POST['flag'] == 3){
		$query = "
			SELECT topic_name, table1.topic_id
			FROM (
				SELECT T.topic_name, T.topic_id
				FROM topics T
			) AS table1
			INNER JOIN
			(
				SELECT S.topic_id
				FROM scores_in S
				GROUP BY S.topic_id
				HAVING COUNT(S.user_id) > 1
			) AS table2
			ON table1.topic_id = table2.topic_id
		";
	}

	($query_run = mysql_query($query))or die(mysql_error());

	$res = array();

	while( ($row = mysql_fetch_assoc($query_run)) )
		$res[] = $row;

	echo json_encode($res);
?>
