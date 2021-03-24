import Graph_InstallCentOS from './Graph.InstallCentOS'
import Graph_InstallRHEL from './Graph.InstallRHEL'
import Graph_InstallWindowsServer from './Graph.InstallWindowsServer'
import Graph_InstallWindowsServer_2016 from './Graph.InstallWindowsServer2016'
import Graph_Raid_Create_AdaptecRAID from './Graph.Raid.Create.AdaptecRAID'
import Graph_Raid_Create_HpssaRAID from './Graph.Raid.Create.HpssaRAID'
import Graph_Raid_Create_PercRAID from './Graph.Raid.Create.PercRAID'

const paramMap = {
    'Graph.InstallCentOS': Graph_InstallCentOS,
    'Graph.InstallRHEL': Graph_InstallRHEL,
    'Graph.InstallWindowsServer': Graph_InstallWindowsServer,
    'Graph.InstallWindowsServer2016': Graph_InstallWindowsServer_2016,
    'Graph.Raid.Create.AdaptecRAID': Graph_Raid_Create_AdaptecRAID,
    'Graph.Raid.Create.HpssaRAID': Graph_Raid_Create_HpssaRAID,
    'Graph.Raid.Create.PercRAID': Graph_Raid_Create_PercRAID,
}

// 继承的 vue 单文件需要特殊判定
const inherit = ['Graph.InstallWindowsServer2016'];

function isInherit(workflow) {
    if (inherit.indexOf(workflow) != -1) {
        return true;
    }
    return false;
}

export {paramMap, isInherit};