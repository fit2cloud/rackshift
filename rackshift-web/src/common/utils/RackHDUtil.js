function isomerismDisk(raidDisk) {
    let raidType = {};
    if (raidDisk && raidDisk.length) {
        for (let i = 0; i < raidDisk.length; i++) {
            if (raidDisk[i].type) {
                if (!raidType[raidDisk[i].type]) {
                    raidType[raidDisk[i].type] = 1;
                }
            } else {
                raidType['unknown'] = 1;
            }
        }
    }

    if (Object.keys(raidType).length > 1) {
        return true;
    }
    return false;
}

export {isomerismDisk}

