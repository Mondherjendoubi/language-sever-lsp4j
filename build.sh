#!/bin/bash

# This script build and installs the launcher.jar to the vscode-plugin directory

gradle build -x test &&
cp -fv ./lsp-server/build/libs/launcher.jar ../VSCode-Language-Client/Launcher/launcher.jar
