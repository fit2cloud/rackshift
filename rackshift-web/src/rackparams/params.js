import Graph_InstallCentOS from './Graph.InstallCentOS'
import Graph_InstallRHEL from './Graph.InstallRHEL'
import Graph_InstallWindowsServer from './Graph.InstallWindowsServer'
import Graph_Raid_Create_AdaptecRAID from './Graph.Raid.Create.AdaptecRAID'
import Graph_Raid_Create_HpssaRAID from './Graph.Raid.Create.HpssaRAID'
import Graph_Raid_Create_PercRAID from './Graph.Raid.Create.PercRAID'
import Graph_InstallUbuntu from './Graph.InstallUbuntu'

const map = {
    'Graph.InstallCentOS': Graph_InstallCentOS,
    'Graph.InstallRHEL': Graph_InstallRHEL,
    'Graph.InstallWindowsServer': Graph_InstallWindowsServer,
    'Graph.Raid.Create.AdaptecRAID': Graph_Raid_Create_AdaptecRAID,
    'Graph.Raid.Create.HpssaRAID': Graph_Raid_Create_HpssaRAID,
    'Graph.Raid.Create.PercRAID': Graph_Raid_Create_PercRAID,
    'Graph.InstallUbuntu': Graph_InstallUbuntu,
}
export default map;