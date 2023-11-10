package com.wearos.coinwatch.data.model;

import com.wearos.coinwatch.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Coin {

    BITCOIN(0, "Bitcoin", "BTC", R.drawable.bitcoin, "bitcoin"),
    ETHEREUM(1, "Ethereum", "ETH", R.drawable.ethereum, "ethereum"),
    TETHER(2, "Tether", "USDT", R.drawable.tether, "tether"),
    USD_COIN(3, "USD Coin", "USDC", R.drawable.usdc, "usd-coin"),
    BNB(4, "BNB", "BNB", R.drawable.bnb, "binancecoin"),
    XRP(5, "XRP", "XRP", R.drawable.xrp, "ripple"),
    BINANCE_USD(6, "Binance USD", "BUSD", R.drawable.binance_usd, "binance-usd"),
    CARDANO(7, "Cardano", "ADA", R.drawable.cardano, "cardano"),
    DOGECOIN(8, "Dogecoin", "DOGE", R.drawable.dogecoin, "dogecoin"),
    POLYGON(9, "Polygon", "MATIC", R.drawable.polygon, "matic-network"),
    POLKADOT(10, "Polkadot", "DOT", R.drawable.polkadot, "polkadot"),
    LITECOIN(11, "Litecoin", "LTC", R.drawable.litecoin, "litecoin"),
    SOLANA(12, "Solana", "SOL", R.drawable.solana, "solana"),
    DAI(13, "Dai", "DAI", R.drawable.dai, "dai"),
    SHIBA_INU(14, "Shiba Inu", "SHIB", R.drawable.shiba_inu, "shiba-inu"),
    CHAINLINK(15, "Chainlink", "LINK", R.drawable.chainlink, "chainlink"),
    MONERO(16, "Monero", "XMR", R.drawable.monero, "monero"),
    ETHEREUM_CLASSIC(17, "Ethereum Classic", "ETC", R.drawable.ethereum_classic, "ethereum-classic"),
    STELLAR(18, "Stellar", "XLM", R.drawable.stellar, "stellar"),
    BITCOIN_CASH(19, "Bitcoin Cash", "BCH", R.drawable.bitcoin_cash, "bitcoin-cash"),
    ALGORAND(20, "Algorand", "ALGO", R.drawable.algorand, "algorand"),
    NEAR_PROTOCOL(21, "Near Protocol", "NEAR", R.drawable.near, "near"),
    TRON(22, "Tron", "TRX", R.drawable.tron, "tron"),
    AVALANCHE(23, "Avalanche", "AVAX", R.drawable.avalanche, "avalanche-2"),
    UNISWAP(24, "Uniswap", "UNI", R.drawable.uniswap, "uniswap"),
    COSMOS_HUB(25, "Cosmos", "ATOM", R.drawable.cosmos, "cosmos"),
    OKB(26, "OKB", "OKB", R.drawable.okb, "okb"),
    FILECOIN(27, "Filecoin", "FIL", R.drawable.filecoin, "filecoin"),
    HEDERA(28, "Hedera", "HBAR", R.drawable.hedera_hashgraph, "hedera-hashgraph"),
    VECHAIN(29, "VeChain", "VET", R.drawable.vechain, "vechain"),
    EOS(30, "Eos", "EOS", R.drawable.eos, "eos"),
    AAVE(31, "Aave", "AAVE", R.drawable.aave, "aave"),
    NEO(32, "Neo", "NEO", R.drawable.neo, "neo"),
    XDC_NETWORK(33, "XDC Network", "XDC", R.drawable.xdc, "xdce-crowd-sale"),
    DASH(34, "Dash", "DASH", R.drawable.dash, "dash"),
    IOTA(35, "Iota", "MIOTA", R.drawable.iota, "iota"),
    PancakeSwap(36, "PancakeSwap", "CAKE", R.drawable.pancakeswap, "pancakeswap-token"),
    CELO(37, "Celo", "CELO", R.drawable.celo, "celo"),
    THETA_FUEL(38, "Theta Fuel", "TFUEL", R.drawable.thetafuel, "theta-fuel"),
    THETA_NETWORK(39, "Theta Network", "THETA", R.drawable.theta, "theta-token"),
    ICON(40, "ICON", "ICX", R.drawable.icon, "icon"),
    WRAPPED_BITCOIN(41, "Wrapped Bitcoin", "WBTC", R.drawable.wrapped_bitcoin, "wrapped-bitcoin"),
    TON_COMMUNITY(42, "Toncoin", "TON", R.drawable.ton, "tontoken"),
    INTERNET_COMPUTER(43, "Internet Computer", "ICP", R.drawable.internet_computer, "internet-computer"),
    TRUE_USD(44, "TrueUSD", "TUSD", R.drawable.trueusd, "true-usd"),
    APTOS(45, "Aptos", "APT", R.drawable.aptos, "aptos"),
    APECOIN(46, "ApeCoin", "APE", R.drawable.apecoin, "apecoin"),
    KUCOIN(48, "KuCoin", "KCS", R.drawable.kucoin, "kucoin-shares"),
    RADIX(48, "Radix", "XRD", R.drawable.radix, "radix"),
    THE_SANDBOX(49, "The Sandbox", "SAND", R.drawable.the_sandbox, "the-sandbox"),
    DECENTRALAND(50, "Decentraland", "MANA", R.drawable.decentraland, "decentraland"),
    TEZOS(51, "Tezos", "XTZ", R.drawable.xtz, "tezos"),
    FLOW(52, "Flow", "FLOW", R.drawable.flow, "flow"),
    AXIE_INFINITY(53, "Axie Infinity", "AXS", R.drawable.axie_infinity, "axie-infinity"),
    ROCKET_POOL(54, "Rocket Pool", "RPL", R.drawable.rocketpool, "rocket-pool"),
    FANTOM(55, "Fantom", "FTM", R.drawable.fantom, "fantom"),
    SEI_NETWORK(56, "Sei", "SEI", R.drawable.sei, "sei-network"),
    OCEAN_PROTOCOL(57, "Ocean Protocol", "OCEAN", R.drawable.ocean, "ocean-protocol"),
    FRAX_SHARE(58, "Frax Share", "FXS", R.drawable.frax_share, "frax-share"),
    TRUST_WALLET(59, "Trust Wallet", "TWT", R.drawable.trust, "trust-wallet-token"),
    ARWEAVE(60, "Arweave", "AR", R.drawable.arweave, "arweave");

    private static final Map<String, Coin> map;

    static {
        Map<String, Coin> coinMap = Arrays.stream(Coin.values())
                .collect(Collectors.toMap(s -> s.queryId, Function.identity()));
        map = Collections.unmodifiableMap(coinMap);
    }

    public static Coin of(String queryId) {
        return Optional.ofNullable(map.get(queryId)).orElseThrow(() ->
                new NullPointerException("Coin type does not exist."));
    }

    private final int id;
    private final String name;
    private final String abbreviation;
    private final int icon;
    private final String queryId;

    Coin(int id, String name, String abbreviation, int icon, String queryId) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.icon = icon;
        this.queryId = queryId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getIcon() {
        return icon;
    }

    public String getQueryId() {
        return queryId;
    }
}
