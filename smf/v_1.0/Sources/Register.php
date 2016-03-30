<?php

/**
EXPROFESSO INTEGRATION CHANGES
all functions - send meactivation message
 */

if (!defined('SMF'))
	die('Hacking attempt...');


function Register($reg_errors = array())
{
	sendRedirToCursus();
}

function Register2($verifiedOpenID = false)
{
	sendRedirToCursus();
}

function Activate()
{
	sendRedirToCursus();
}

?>