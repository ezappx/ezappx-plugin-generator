#!/usr/bin/env bash

echo "compile ezappx-plugin-template..."
npm run build
echo "copy dist/ezappx-plugin-template.min.js to local-ezappx-project-js-dir"
cp dist/ezappx-plugin-template.min.js local-ezappx-project-js-dir
echo "done"