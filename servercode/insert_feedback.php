<?php
	require 'core.inc.php';
	require 'connect.inc.php';

	$user_id = $_POST['user_id'];
	$rating = $_POST['rating'];
	$content = $_POST['content'];

	$query = "
		SELECT rating
		FROM feedback
		WHERE user_id = $user_id
		AND rating = $rating
	";

	( $query_run = mysql_query($query) ) or die(mysql_error());

	if (mysql_num_rows($query_run) == 0) {
		$query = "
			INSERT INTO `feedback` VALUES (
				'$user_id',
				'$rating',
				'$content'
			)
		";

		(mysql_query ( $query )) or die(mysql_error());
		echo "Thanks for your feedback !";
		
	} else {
		echo "Please submit a different rating";
	}

?>
