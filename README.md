#LiquidEnergy
**Current MC ver:** `1.4.7 release`

**API vers:** see `common/API_VERSIONS`

Converting energy (EU/MJ) to a Forge liquid and back again.

##License
This mod itself is licensed under the MIT License (See `LICENSE.md`).

It includes the Buildcraft API and IC2 API. More details are available in `LICENSE.md`

##Building
Simply call `ant` in the build directory to build.

##Developing

###IntelliJ IDEA 12
IDEA supports pulling source from multiple folders, so open the IDEA project (`{Repo}/IDEA`) and use Ant to run the `setup` target. This will setup Forge/FML/MCP and import source from the common/ folder. This approach is also safe from the Windows bug in the Eclipse setup.

Run configurations need to be added manually due to where they are stored. In Edit Configurations, click the Add New Configuration button then select Application. The values for client and server are provided below.

####Client
Main class: `Start`

VM options: `-Djava.library.path=bin/natives` (you can add -Xms/-Xmx here too)

Working directory: `../build/forge/mcp/jars`

Optionally tick 'Single instance only' to force IDEA to only launch one instance.

####Server
Main class: `net.minecraft.server.dedicated.DedicatedServer`

VM options: Blank; optionally add -Xms/-Xmx flags.

Program arguments: `nogui` (unless you *want* that daft GUI)

Working directory: `../build/forge/mcp/jars`

Optionally tick 'Single instance only' to force IDEA to only launch one instance.

##Eclipse
For development, call `ant linksrc` to automatically setup Forge/MCP and symlink the source into place. This is completely safe on UNIX systems. Note that on Windows, this uses Junctions which can cause issues. Remove them by hand before running a real build. This warning is repeated during the link.