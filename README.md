#LiquidEnergy
**Current MC ver:** `1.4.7 release`

**API vers:** see `common/API_VERSIONS`

Converting energy (EU/MJ) to a Forge liquid and back again.

##License
This mod itself is licensed under the MIT License (See `LICENSE.md`).

It includes the Buildcraft API and IC2 API, along with some external classes. More details are available in `LICENSE.md`

##Building
Simply call `ant` in the build directory to build.

Feeling lazy, or don't have the JDK+Ant? We have automated development builds on our [Bamboo CI](https://bamboo.azurenode.net/browse/LIQEN-PACK/latest/artifact) server. These are built with JDK7, so may only run on Java 7 or above.

##Developing

###IntelliJ IDEA 12
IDEA supports pulling source from multiple folders, so open the repo folder as an IDEA project and use Ant to run the `setup` target. This will setup Forge/FML/MCP and import source from the common/ folder. This approach is also safe from the Windows bug in the Eclipse setup.

Run configurations are now stored in the IDEA project file, so shouldn't need to be recreated.

##Eclipse
For development, call `ant linksrc` to automatically setup Forge/MCP and symlink the source into place. This is completely safe on UNIX systems. Note that on Windows, this uses Junctions which can cause issues. Remove them by hand before running a real build. This warning is repeated during the link.
