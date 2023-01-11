DROP FUNCTION get_messages();
CREATE OR REPLACE FUNCTION get_messages() RETURNS setof record
language plpgsql
as
$$
DECLARE
f_id integer := %s;
t_id integer := %s;
start_ bigint := %s;
end_ bigint := %s;

rec_ record;

BEGIN

for rec_ in (WITH MSG AS
(
SELECT
	created,
	from_id,
	to_id,
	data,
	ROW_NUMBER() OVER() AS row_number
FROM
    messages
WHERE
	from_id = f_id AND
	to_id = t_id

UNION

SELECT
	created,
	from_id,
	to_id,
	data,
	ROW_NUMBER() OVER() AS row_number
FROM
    messages
WHERE
	from_id = t_id AND
	to_id = f_id

ORDER BY created DESC
)
SELECT
    created,
	from_id,
	to_id,
	data,
	ROW_NUMBER() OVER() AS row_number,
	start_,
	end_
FROM
    MSG)
loop
	return next rec_;
end loop;

return;

END
$$;

SELECT * FROM get_messages() as (created TIMESTAMP, from_id int, to_id int, data text, index_ bigint, start_ bigint, end_ bigint)
WHERE index_ BETWEEN start_ and end_;