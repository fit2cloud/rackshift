DELETE
FROM
    network_card
WHERE
        bare_metal_id NOT IN (SELECT id FROM bare_metal);