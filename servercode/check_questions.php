<?php
	require 'core.inc.php';
	require 'connect.inc.php';
	
		
		$user_id = $_GET['user_id'];
		$topic_id = $_GET['topic_id'];
		
		foreach ($_POST as $question_id => $user_answer){
			//echo "$question_id \t $user_answer<br />";
			
			$query = "
				SELECT user_id
				FROM answers
				WHERE user_id = $user_id
				AND question_id = $question_id
			";
			
			( $query_run = mysql_query($query) ) or die( mysql_error() );
			
			$check = mysql_num_rows($query_run);
			
			if ($check == 0){
				$query = "
					INSERT INTO answers VALUES (
						$user_id,
						$question_id,
						$user_answer,
						$user_answer IN (
							SELECT answer
							FROM questions
							WHERE question_id = $question_id
						)
					)
				";
			} else {
				$query = "
					UPDATE answers
					SET
						user_answer = $user_answer,
						correctness = ( 
								$user_answer IN (
									SELECT answer
									FROM questions
									WHERE question_id = $question_id
								) 
							)
					WHERE user_id = $user_id
					AND question_id = $question_id
				";
			}
			
			( mysql_query($query) ) or die( mysql_error() );
			
		}
		
		$query = "
			SELECT user_id, topic_id, SUM(points) FROM
			(
				SELECT A.user_id, Q.question_id, Q.topic_id, Q.difficulty
				FROM questions Q, answers A
				WHERE Q.question_id IN (
						SELECT question_id
						FROM answers
						WHERE correctness = 1
						AND user_id = $user_id
				)
				AND A.question_id = Q.question_id
			) AS table1
			INNER JOIN
			(
				SELECT difficulty, points
				FROM weightage
			) AS table2
			ON table1.difficulty = table2.difficulty
			AND user_id = $user_id
			AND topic_id = $topic_id
		"; 
		
		( $query_run = mysql_query($query) ) or die( mysql_error() ); // running query to get scores
		
		$scores = array();
		while( $row = mysql_fetch_assoc($query_run) )
			$scores[] = $row;	// scores array now contains elements, with key as topic_id and value as score in tht topic.
		
		//print_r($scores);
		
		// Updating scores_in...if topic attempted for the first time (INSERT) or not(UPDATE). // Note that for loop runs only one time.
		foreach ($scores as $row_number => $data){
			$score = $data['SUM(points)'];
			
			//echo $score . "<br>";
			
			$query = "
				SELECT user_id
				FROM scores_in
				WHERE user_id = $user_id
				AND topic_id = $topic_id
			";
			
			( $query_run = mysql_query($query) ) or die( mysql_error() );
			
			$check = mysql_num_rows($query_run);
			
			if ($check == 0){
				$query = "
					INSERT INTO scores_in VALUES (
						$user_id,
						$topic_id,
						$score
					)
				";
			} else {
				$query = "
					UPDATE scores_in
					SET
						score = $score
					WHERE user_id = $user_id
					AND topic_id = $topic_id
				";
			}
			
			( mysql_query($query) ) or die( mysql_error() );
			
		}
		
		$results = array();
		foreach ($_POST as $question_id => $user_answer){
			$query = "
				SELECT question_id, q_statement, option1, option2, option3, option4, user_answer, answer, correctness, points
				FROM
				(
					SELECT Q.question_id, Q.q_statement, Q.option1, Q.option2, Q.option3, Q.option4, A.user_answer, A.correctness, Q.answer, Q.difficulty
					FROM questions Q, answers A
					WHERE A.user_id = $user_id
					AND Q.question_id = $question_id
					AND A.question_id = $question_id
				) AS table1
				INNER JOIN
				(
					SELECT points, difficulty
					FROM weightage
				) AS table2
				ON
				table1.difficulty = table2.difficulty;
			";
			
			( $query_run = mysql_query($query) ) or die( mysql_error() );

			while( $row = mysql_fetch_assoc($query_run) )
				$results[] = $row;
		}
		
		echo json_encode($results);	// try to find a better solution.
		
		//Changing total score session
		$query = "
			SELECT SUM(score) AS t_score
			FROM scores_in
			WHERE user_id = $user_id
		";

		( $query_run = mysql_query($query) ) or die(mysql_error());

		$t_score = mysql_result($query_run, 0, 't_score');
		//echo $t_score;
	
?>
