UPDATE workflow
SET default_params = '{
        "options": {
          "bootstrap-rancher": {
            "dockerFile": "secure.erase.docker.tar.xz"
          },
          "delete-raid": {
            "path": "/opt/MegaRAID/perccli/perccli64"
          }
        }
      }'
WHERE
	injectable_name = 'Graph.Raid.Delete.MegaRAID';