package netty.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


public class EchoClient {
    private  final String host;
    private  final  int port;
    public EchoClient(String host,int port)
    {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                                ch.pipeline().addLast(new EchoClientHandle());
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();

        }

    }

    public static void main(String[] args) throws Exception{

if( args.length != 2){
    System.err.println(
            "Usage:"+EchoClient.class.getSimpleName()+
                    "<host> <port>");
    return;
}
String host = args[0];
int port = Integer.parseInt(args[1]);
new EchoClient(host,port).start();
    }
}

