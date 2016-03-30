<?php

if (!defined('SMF'))
	die('Hacking attempt...');

function integrate_exprofesso_user() {
	
	$ret = file_get_contents("http://localhost:9090/cursus2/userid?ext=" . $_COOKIE['EXTSESSION']);

	return $ret;
}

function sendRedirToCursus() {
        header('Location: /cursus2/');
        // exit;
}

?>

