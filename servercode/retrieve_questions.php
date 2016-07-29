<?php
	require 'core.inc.php';
	require 'connect.inc.php';

	$topic_id = $_POST['topic_id'];

	$query = "
			SELECT question_id, q_statement, option1, option2, option3, option4
			FROM questions
			WHERE topic_id = $topic_id
	";

	($query_run = mysql_query($query)) or die(mysql_error());

	$res = array();

	$count = 0;

	while ( ( $row = mysql_fetch_assoc($query_run) ) && $count < 10 ) {	// random questions ?
		$res[] = $row;
		$count++;
	}

	echo json_encode($res);
?>