module.exports = {
    productionSourceMap: true,
    configureWebpack: {
        devtool: 'source-map'
    },
    devServer: {
        host: "localhost",
        port: 8080,
        proxy: {
            "/": {
                target: 'http://localhost:8082',
                ws: true,
                changeOrigin: true
            }
        }
    }
};
