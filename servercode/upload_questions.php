<?php
	require 'core.inc.php';
	require 'connect.inc.php';

	$q_statement = $_POST['question'];
	$option1 = $_POST['optA'];
	$option2 = $_POST['optB'];
	$option3 = $_POST['optC'];
	$option4 = $_POST['optD'];
	$topic_name = $_POST['topic'];
	$weightage = $_POST['weightage'];
	$correct = $_POST['correct'];

	$query = "
		SELECT topic_id
		FROM topics
		WHERE topic_name = '$topic_name'
	";

	($query_run = mysql_query($query)) or die(mysql_error());
	$topic_id = mysql_result($query_run, 0);

	$query = "
		INSERT INTO questions VALUES (
			'',
			$weightage,
			$topic_id,
			'$q_statement',
			'$option1',
			'$option2',
			'$option3',
			'$option4',
			$correct
		)
	";

	($query_run = mysql_query($query)) or die(mysql_error());

	echo $topic_id;
?>
