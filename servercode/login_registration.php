<?php
	require 'core.inc.php';
	require 'connect.inc.php';
	
	if (isset($_POST['login'])){
		
		if (isset ( $_POST ['username'] ) && isset ( $_POST ['password'] )) {

			$username = $_POST ['username'];
			$password = $_POST ['password'];

			$query = "
				SELECT `user_id`
				FROM `users`
				WHERE `username`='$username'
				AND `password`='" . md5 ( $password ) . "'
			";

			($query_run = mysql_query ( $query )) or die(mysql_error());
			
			$num_rows = mysql_num_rows ( $query_run );

			if ($num_rows == 0) {
 				echo "<script>alert('Invalid Username or Password'); window.location.href = 'index.html'</script>";
			} else {
				$user_id = mysql_result ( $query_run, 0, 'user_id' );
				$_SESSION ['user_id'] = $user_id;

				//For Total Score:
				$query = "
					SELECT user_id
					FROM scores_in
					WHERE user_id = $user_id
				";

				( $query_run = mysql_query($query) ) or die(mysql_error());

				$check = mysql_num_rows($query_run);
				
				if($check == 0) {
					$_SESSION['t_score'] = 0;
				} else {

					$query = "
						SELECT SUM(score) AS t_score
						FROM scores_in
						WHERE user_id = $user_id
					";

					( $query_run = mysql_query($query) ) or die(mysql_error());

					$t_score = mysql_result($query_run, 0, 't_score');
					$_SESSION ['t_score'] = $t_score;
				}
				
				header ( 'Location: user_home_page.php' );
			}
		}
	} else if (isset($_POST['register'])){
		
        if (isset ( $_POST ['username'] ) && isset ( $_POST ['password'] ) && isset ( $_POST ['r_password'] )) {
			
            $username = $_POST ['username'];
			$pass = $_POST ['password'];
			$check_pass = $_POST ['r_password'];
			$email_id = $_POST ['email_id'];
			$ph_num = $_POST ['ph_number'];
			$gender = $_POST ['gender'];
			
			if ($pass != $check_pass)
				echo '<script>alert("Password dont match. Re-enter password."); window.location.href = "index.html"</script>';
			else {
			
				$query = "
					SELECT `username`
					FROM `users`
					WHERE `username`='$username'
				";
				
				($query_run = mysql_query ( $query )) or die(mysql_error());
				
				$num_rows = mysql_num_rows ( $query_run );

				if ($num_rows == 1) {
					echo '<script>alert("Username already exists."); window.location.href = "index.html"</script>';
				} else {

					$query = "
						INSERT INTO `users` VALUES (
							'',
							'$username',
							'" . md5 ( $pass ) . "',
							'$email_id',
							'$gender',
							'$ph_num'
						)
					";
					
					(mysql_query ( $query )) or die(mysql_error());

					$query = "
						SELECT `user_id`
						FROM `users`
						WHERE `username`='$username'
					";
					
					($query_run = mysql_query ( $query )) or die(mysql_error());
					$user_id = mysql_result ( $query_run, 0, 'user_id' );
					
					$_SESSION ['user_id'] = $user_id;
					$_SESSION ['t_score'] = 0;
					
					header ( 'Location: user_home_page.php' );
				
				}
			}
		}
	}
?>