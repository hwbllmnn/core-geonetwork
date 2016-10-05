UPDATE metadata
SET data = regexp_replace(data, '<bfs:inspireID.*</bfs:inspireID>', E'<bfs:legendTitle gco:nilReason="missing">\n        <gco:CharacterString />\n      </bfs:legendTitle>', 'g')
WHERE data NOT LIKE '%legendTitle%'
AND schemaid = 'iso19139.bfs';

UPDATE metadata
SET data = regexp_replace(data, '</bfs:legendTitle>', E'</bfs:legendTitle>\n      <bfs:printTitle gco:nilReason="missing">\n        <gco:CharacterString />\n      </bfs:printTitle>', 'g')
WHERE data NOT LIKE '%printTitle%'
AND schemaid = 'iso19139.bfs';
