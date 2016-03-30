<?php

/**
EXPROFESSO INTEGRATION CHANGES
all functions - send redirect to /cursus2
 */

if (!defined('SMF'))
	die('Hacking attempt...');


function ModifyProfile($post_errors = array())
{
	sendRedirToCursus();
}

// Load any custom fields for this area... no area means load all, 'summary' loads all public ones.
function loadCustomFields($memID, $area = 'summary')
{
	global $context, $txt, $user_profile, $smcFunc, $user_info, $settings, $scripturl;

	// Get the right restrictions in place...
	$where = 'active = 1';
	if (!allowedTo('admin_forum') && $area != 'register')
	{
		// If it's the owner they can see two types of private fields, regardless.
		if ($memID == $user_info['id'])
			$where .= $area == 'summary' ? ' AND private < 3' : ' AND (private = 0 OR private = 2)';
		else
			$where .= $area == 'summary' ? ' AND private < 2' : ' AND private = 0';
	}

	if ($area == 'register')
		$where .= ' AND show_reg != 0';
	elseif ($area != 'summary')
		$where .= ' AND show_profile = {string:area}';

	// Load all the relevant fields - and data.
	$request = $smcFunc['db_query']('', '
		SELECT
			col_name, field_name, field_desc, field_type, field_length, field_options,
			default_value, bbc, enclose, placement
		FROM {db_prefix}custom_fields
		WHERE ' . $where,
		array(
			'area' => $area,
		)
	);
	$context['custom_fields'] = array();
	while ($row = $smcFunc['db_fetch_assoc']($request))
	{
		// Shortcut.
		$exists = $memID && isset($user_profile[$memID], $user_profile[$memID]['options'][$row['col_name']]);
		$value = $exists ? $user_profile[$memID]['options'][$row['col_name']] : '';

		// If this was submitted already then make the value the posted version.
		if (isset($_POST['customfield']) && isset($_POST['customfield'][$row['col_name']]))
		{
			$value = $smcFunc['htmlspecialchars']($_POST['customfield'][$row['col_name']]);
			if (in_array($row['field_type'], array('select', 'radio')))
					$value = ($options = explode(',', $row['field_options'])) && isset($options[$value]) ? $options[$value] : '';
		}

		// HTML for the input form.
		$output_html = $value;
		if ($row['field_type'] == 'check')
		{
			$true = (!$exists && $row['default_value']) || $value;
			$input_html = '<input type="checkbox" name="customfield[' . $row['col_name'] . ']" ' . ($true ? 'checked="checked"' : '') . ' class="input_check" />';
			$output_html = $true ? $txt['yes'] : $txt['no'];
		}
		elseif ($row['field_type'] == 'select')
		{
			$input_html = '<select name="customfield[' . $row['col_name'] . ']"><option value="-1"></option>';
			$options = explode(',', $row['field_options']);
			foreach ($options as $k => $v)
			{
				$true = (!$exists && $row['default_value'] == $v) || $value == $v;
				$input_html .= '<option value="' . $k . '"' . ($true ? ' selected="selected"' : '') . '>' . $v . '</option>';
				if ($true)
					$output_html = $v;
			}

			$input_html .= '</select>';
		}
		elseif ($row['field_type'] == 'radio')
		{
			$input_html = '<fieldset>';
			$options = explode(',', $row['field_options']);
			foreach ($options as $k => $v)
			{
				$true = (!$exists && $row['default_value'] == $v) || $value == $v;
				$input_html .= '<label for="customfield_' . $row['col_name'] . '_' . $k . '"><input type="radio" name="customfield[' . $row['col_name'] . ']" class="input_radio" id="customfield_' . $row['col_name'] . '_' . $k . '" value="' . $k . '" ' . ($true ? 'checked="checked"' : '') . '>' . $v . '</label><br />';
				if ($true)
					$output_html = $v;
			}
			$input_html .= '</fieldset>';
		}
		elseif ($row['field_type'] == 'text')
		{
			$input_html = '<input type="text" name="customfield[' . $row['col_name'] . ']" ' . ($row['field_length'] != 0 ? 'maxlength="' . $row['field_length'] . '"' : '') . ' size="' . ($row['field_length'] == 0 || $row['field_length'] >= 50 ? 50 : ($row['field_length'] > 30 ? 30 : ($row['field_length'] > 10 ? 20 : 10))) . '" value="' . $value . '" class="input_text" />';
		}
		else
		{
			@list ($rows, $cols) = @explode(',', $row['default_value']);
			$input_html = '<textarea name="customfield[' . $row['col_name'] . ']" ' . (!empty($rows) ? 'rows="' . $rows . '"' : '') . ' ' . (!empty($cols) ? 'cols="' . $cols . '"' : '') . '>' . $value . '</textarea>';
		}

		// Parse BBCode
		if ($row['bbc'])
			$output_html = parse_bbc($output_html);
		elseif($row['field_type'] == 'textarea')
			// Allow for newlines at least
			$output_html = strtr($output_html, array("\n" => '<br />'));

		// Enclosing the user input within some other text?
		if (!empty($row['enclose']) && !empty($output_html))
			$output_html = strtr($row['enclose'], array(
				'{SCRIPTURL}' => $scripturl,
				'{IMAGES_URL}' => $settings['images_url'],
				'{DEFAULT_IMAGES_URL}' => $settings['default_images_url'],
				'{INPUT}' => $output_html,
			));

		$context['custom_fields'][] = array(
			'name' => $row['field_name'],
			'desc' => $row['field_desc'],
			'type' => $row['field_type'],
			'input_html' => $input_html,
			'output_html' => $output_html,
			'placement' => $row['placement'],
			'colname' => $row['col_name'],
			'value' => $value,
		);
	}
	$smcFunc['db_free_result']($request);
}

?>