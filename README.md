![Logo](la-images.png) LA-iMageS [![license](https://img.shields.io/badge/LICENSE-GPLv3-blue.svg)]() [![DOI](https://zenodo.org/badge/DOI/10.5281/zenodo.166500.svg)](https://doi.org/10.5281/zenodo.166500)
=================================
Laser ablation and inductively coupled plasma mass spectrometry focusing on bioimaging from elemental distribution. Please, visit the [official web page ](http://www.la-images.net) of the project for downloads and support.

Modules
-------
This maven project is comprised of the following modules:
* core: Contains the default implementation API.
* gui: Contains several GUI components.
* aibench: Contains a GUI application based on AIBench framework ([AIBench](http://www.aibench.org/)).

Building LA-iMageS
----
The final application can be built using the `mvn install` command. This command builds the three modules and generates the AIBench based application at `la-images-aibench/target`.

In order to create the Windows/Linux portable versions and the Windows executable installers, two profiles can be specified: `createDistributables` and `createInstallers`, respectively. Both portable versions and installers are self-contained since they contain an embedded Java Runtime Environment to run the application.

### Building the portable versions
Windows/Linux portable versions can be built using the `mvn install -PcreateDistributables` command. This command generates the following four portable versions of La-iMAgeS at `la-images-aibench/target`: Linux 32-bit, Linux 64-bit, Windows 32-bit and Windows 64-bit.

### Building the Windows installers
NSIS-based Windows installers can be built using the `mvn install -PcreateInstallers` command. This command generates the 32 and 64-bit versions at `la-images-aibench/target/dist32` and `la-images-aibench/target/dist`, respectively. Note that this profile is created to run under Linux environments with `mingw-w64` and `wine` installed (it relies on `i686-w64-mingw32-windres` and `i686-w64-mingw32-gcc` to create some application files packed by NSIS).

Team
----
This project is an idea and is developed by:
* Marco Aurélio Zezzi Arruda [GEPAM Group](http://gepam.iqm.unicamp.br/)
* Gustavo de Souza Pessôa [GEPAM Group](http://gepam.iqm.unicamp.br/)
* José Luis Capelo Martínez [Bioscope Group](http://www.bioscopegroup.org/)
* Florentino Fdez-Riverola [SING Group](http://sing.ei.uvigo.es)
* Miguel Reboiro-Jato [SING Group](http://sing.ei.uvigo.es)
* Hugo López-Fdez [SING Group](http://sing.ei.uvigo.es)
* Daniel Glez-Peña [SING Group](http://sing.ei.uvigo.es)
