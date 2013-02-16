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
For development, call `ant linksrc` to automatically setup Forge/MCP and symlink the source into place. This is completely safe on UNIX systems. Note that on Windows, this uses Junctions which can cause issues. Remove them by hand before running a real build. This warning is repeated during the link.