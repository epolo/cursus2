<?php

/**
EXPROFESSO INTEGRATION CHANGES
all functions - send redirect to /cursus2
 */

if (!defined('SMF'))
	die('Hacking attempt...');

function Login()
{
	sendRedirToCursus2();
}

function Login2()
{
	sendRedirToCursus2();
}

function checkActivation()
{
	sendRedirToCursus2();
}

function Logout($internal = false, $redirect = true)
{
	sendRedirToCursus2();
}

function sendRedirToCursus2() {
	header('Location: /cursus2/');
	// exit;
}

?>