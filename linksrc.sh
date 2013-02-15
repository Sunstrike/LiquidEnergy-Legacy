SCRIPTPATH=$( cd $(dirname $0) ; pwd -P )

echo "(bash) Linking source with ln..."
ln -s $SCRIPTPATH/common/io $SCRIPTPATH/build/forge/mcp/src/minecraft/io
