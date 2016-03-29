<?php

if (!defined('SMF'))
	die('Hacking attempt...');

function integrate_exprofesso_user() {
	
	$ret = file_get_contents("http://localhost:9090/cursus2/userid?ext=" . $_COOKIE['EXTSESSION']);

		// '2';

	// print ' -- called integrate_exprofesso_user() -- ' . $ret;
	// print_r($_COOKIE);

	return $ret;
}

?>

