<?php

/**
EXPROFESSO INTEGRATION CHANGES
all functions - send redirect to /cursus2
 */

if (!defined('SMF'))
	die('Hacking attempt...');

function Login()
{
	sendRedirToCursus();
}

function Login2()
{
	sendRedirToCursus();
}

function checkActivation()
{
	sendRedirToCursus();
}

function Logout($internal = false, $redirect = true)
{
	sendRedirToCursus();
}

?>