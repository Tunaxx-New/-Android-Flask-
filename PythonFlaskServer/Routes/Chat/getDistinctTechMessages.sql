SELECT
	DISTINCT(from_id)
FROM
	messages
WHERE
	to_id = %s;