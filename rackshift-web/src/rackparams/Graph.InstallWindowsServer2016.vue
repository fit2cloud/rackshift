<template>
  <Graph-Install-Windows-Server :bareMetalId="bareMetalId" :extraParamas="extraParams"
                                :params="params"></Graph-Install-Windows-Server>
</template>
<script>
import GraphInstallWindowsServer from './Graph.InstallWindowsServer'

export default {
  props: ['bareMetalId', 'extraParams', 'params'],
  components: {
    GraphInstallWindowsServer
  }
}
</script>