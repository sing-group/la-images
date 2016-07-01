# Modern UI example script
!include MUI.nsh
!include UAC.nsh
Name "LA-iMageS"
OutFile "setup.exe"
InstallDir "$PROGRAMFILES\LA-iMageS"

;Get installation folder from registry if available
InstallDirRegKey HKCU "Software\LA-iMageS" ""

;Request application privileges for Windows Vista
RequestExecutionLevel user

;--------- UAC STUFF ------------
!macro Init thing
uac_tryagain:
!insertmacro UAC_RunElevated
${Switch} $0
${Case} 0
	${IfThen} $1 = 1 ${|} Quit ${|} ;we are the outer process, the inner process has done its work, we are done
	${IfThen} $3 <> 0 ${|} ${Break} ${|} ;we are admin, let the show go on
	${If} $1 = 3 ;RunAs completed successfully, but with a non-admin user
		MessageBox mb_YesNo|mb_IconExclamation|mb_TopMost|mb_SetForeground "This ${thing} requires admin privileges, try again" /SD IDNO IDYES uac_tryagain IDNO 0
	${EndIf}
	;fall-through and die
${Case} 1223
	MessageBox mb_IconStop|mb_TopMost|mb_SetForeground "This ${thing} requires admin privileges, aborting!"
	Quit
${Case} 1062
	MessageBox mb_IconStop|mb_TopMost|mb_SetForeground "Logon service not running, aborting!"
	Quit
${Default}
	MessageBox mb_IconStop|mb_TopMost|mb_SetForeground "Unable to elevate, error $0"
	Quit
${EndSwitch}
 
SetShellVarContext all
!macroend
 
Function .onInit
!insertmacro Init "installer"
FunctionEnd
 
Function un.onInit
!insertmacro Init "uninstaller"
FunctionEnd
;--------- END UAC STUFF ------------


Var StartMenuFolder

!define MUI_ABORTWARNING
!define MUI_ICON "la-images.ico"
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "la-images-header.bmp"
!define MUI_HEADERIMAGE_RIGHT
!define MUI_WELCOMEFINISHPAGE_BITMAP "installer-splash.bmp"

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "license.txt"
!insertmacro MUI_PAGE_DIRECTORY
;Start Menu Folder Page Configuration
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\LA-iMageS" 
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"

  
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder
 
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_INSTFILES

Function LaunchLaImages
  ExecShell "" "$SMPROGRAMS\$StartMenuFolder\LA-iMageS.lnk"
FunctionEnd

!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT "Run LA-iMages"
!define MUI_FINISHPAGE_RUN_FUNCTION "LaunchLaImages"
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_LANGUAGE "English"

Section "Extract LA-iMageS"
  SectionIn RO
  
  ;Delete "$INSTDIR\plugins_bin\*"
  RMDir /r "$INSTDIR\plugins_bin"
  
  ;Delete "$INSTDIR\${jre.simplepath}\*"
  RMDir /r "$INSTDIR\${jre.simplepath}"
  
  ;Delete "$INSTDIR\lib\*"
  RMDir /r "$INSTDIR\lib"
  
  ;Delete "$INSTDIR\conf\*"
  RMDir /r "$INSTDIR\conf"
  
  ;Delete "$INSTDIR\help\*"
  RMDir /r "$INSTDIR\help"
  
  RMDir /r "$INSTDIR\plugins_install"
  
  SetOutPath $INSTDIR
  File la-images.exe
  File /r ..\plugins_bin
  File /r /x ${target.exluded.dirs} ..\lib 
  File /r ..\conf
  File /r ..\help
  File /r ${jre.64b.path}
  CreateDirectory $INSTDIR\plugins_install
  
  ;Store installation folder
  WriteRegStr HKCU "Software\LA-iMageS" "" $INSTDIR
  
  ;Create uninstaller
  WriteUninstaller "$INSTDIR\Uninstall.exe"
  
  ;Add to add/remove programs registry entries
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "DisplayName" "LA-iMageS"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "UninstallString" "$\"$INSTDIR\Uninstall.exe$\""
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "QuietUninstallString" "$\"$INSTDIR\Uninstall.exe$\" /S"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "DisplayIcon" "$\"$INSTDIR\la-images.exe$\""
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "Publisher" "SING group"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "HelpLink" "http://sing.ei.uvigo.es/la-images"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "URLInfoAbout" "http://sing.ei.uvigo.es/la-images"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "DisplayVersion" "${version}"
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "NoModify" 1
  WriteRegStr HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS" "NoRepair" 1
  
	!insertmacro MUI_STARTMENU_WRITE_BEGIN Application	
   CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
  CreateShortcut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\Uninstall.exe"
  CreateShortcut "$SMPROGRAMS\$StartMenuFolder\LA-iMageS.lnk" "$INSTDIR\la-images.exe"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd



Section "Uninstall"

  ;Delete "$INSTDIR\plugins_bin\*"
  RMDir /r "$INSTDIR\plugins_bin"
  
  ;Delete "$INSTDIR\${jre.simplepath}\*"
  RMDir /r "$INSTDIR\${jre.simplepath}"
  
  ;Delete "$INSTDIR\lib\*"
  RMDir /r "$INSTDIR\lib"
  
  ;Delete "$INSTDIR\conf\*"
  RMDir /r "$INSTDIR\conf"
  
  ;Delete "$INSTDIR\help\*"
  RMDir /r "$INSTDIR\help"
  
  RMDir /r "$INSTDIR\plugins_install"
  
  Delete "$INSTDIR\Uninstall.exe"
  Delete "$INSTDIR\la-images.exe"
  Delete "$INSTDIR\LA-iMageS.log"

  RMDir "$INSTDIR"
  
  !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
    
  Delete "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk"
  Delete "$SMPROGRAMS\$StartMenuFolder\LA-iMageS.lnk"
  RMDir /r "$SMPROGRAMS\$StartMenuFolder"
  DeleteRegKey /ifempty HKCU "Software\LA-iMageS"
  
  ;Delete add/remove programs registry entry
  DeleteRegKey HKCU "Software\Microsoft\Windows\CurrentVersion\Uninstall\LA-iMageS"

SectionEnd