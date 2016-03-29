<?php

if (!defined('SMF'))
	die('Hacking attempt...');

add_integration_function('integrate_pre_include', '$sourcedir/exprofesso/integrate.php',TRUE);
add_integration_function('integrate_verify_user','integrate_exprofesso_user',TRUE);

print 'ExProfesso is successfully integrated';

die(' Install exprofesso integration hooks -- run once! ');

?>

