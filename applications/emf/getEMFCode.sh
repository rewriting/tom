#!/bin/sh

export ECLIPSE_WORKSPACE=${HOME}/workspace/jtom/journeeRII

cp -R ${ECLIPSE_WORKSPACE}/LigneProduitsTelephones/src/ligneproduitstelephones examples/1lignedeproduits

cp mappings/ligneproduitstelephonesmapping.tom examples/1lignedeproduits
cp bin/EcoreContainmentIntrospector.class examples/1lignedeproduits

