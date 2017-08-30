const webpack = require('webpack');

const isProduction = (process.env.NODE_ENV === 'production');

const commonModule = {
    loaders: [
        { test: /\.js$/,
            include: __dirname + '/pages', 
            loader: 'babel-loader', 
            options: {
                presets: ['es2015']
            } 
        },
        {
            test: /\.css$/,
            use: [
                { loader: 'style-loader' },
                { loader: 'css-loader' }
            ]
        },
        {
            test: /\.(png|gif)$/,
            use: [
                { loader: 'url-loader' }
            ]
        }
    ]
};

const commonPlugins = [
    new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery',
        'window.$': 'jquery'
    })
];

const prodConf = {
    devtool: 'source-map',
    entry: [
        './pages/entry-prod.js'
    ],
    output: {
        path: __dirname + '/../resources/static',
        filename: 'index.js'
    },
    module: commonModule,
    plugins: commonPlugins
};

const devBundleConf = {
    devtool: 'cheap-module-eval-source-map',
    entry: [
        './pages/main.js'
    ],
    output: {
        path: __dirname + '/build',
        publicPath: '/build/',
        filename: 'bundle.js',
        libraryTarget: 'var',
        library: 'CssReport'
    },
    module: commonModule,
    plugins: commonPlugins
};

const devEntryConf = {
    devtool: 'cheap-module-eval-source-map',
    entry: [
        './pages/entry-dev.js'
    ],
    output: {
        path: __dirname + '/../resources/static',
        filename: 'index.js'
    }
};

module.exports =
    isProduction
        ? prodConf
        : [ devBundleConf, devEntryConf ];

