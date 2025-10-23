# 目录

## 网址

[docs.searxng.org](https://docs.searxng.org/admin/installation-docker.html#installation-docker)

## 镜像

```text
docker pull searxng/searxng:latest
```

## 安装 (2选1)

### Run安装

```text
docker run -p 6080:8080 \
        --name searxng \
        -d--restart=always \
        -v"/Volumes/lee/docker/SearXNG:/etc/searxng" \
        -e"BASE URL=http://localhost:$PORT/" \
        -e"INSTANCE NAME=lee-instance" \
        searxng/searxng
```

### DockerCompose安装

```text
version: '3.8'

services:
  searxng:
    image: searxng/searxng
    container_name: searxng
    restart: always
    ports:
      - "6080:8080"
    volumes:
      - "../data/searxng/data:/etc/searxng"
    environment:
      - BASE_URL=http://localhost:$PORT/
      - INSTANCE_NAME=lee-instance
#    network_mode: "host"
```

`如果使用虚拟机，记得网络一个默认NAT，一个仅主机，两张网卡`

```text
## 确保能上网，才能查询
[root@localhost app]# docker exec -it searxng ping -c 3 www.qq.com     
PING www.qq.com (121.14.77.201): 56 data bytes
64 bytes from 121.14.77.201: seq=0 ttl=1 time=11.400 ms
64 bytes from 121.14.77.201: seq=1 ttl=1 time=13.113 ms
64 bytes from 121.14.77.201: seq=2 ttl=1 time=13.742 ms

--- www.qq.com ping statistics ---
3 packets transmitted, 3 packets received, 0% packet loss
round-trip min/avg/max = 11.400/12.751/13.742 ms
```

## 访问页面

http://localhost:6080

## 请求注意 

### 浏览器

正常

### APIFox OR POSTMAN

出现 500 错误，后来在浏览器cURL复制了，在导入了APIFox里面。发现`请求头的Cookie`有变化

```text
categories=general; language=zh-CN; locale=zh-Hans-CN; autocomplete=; favicon_resolver=; image_proxy=0; method=POST; safesearch=0; theme=simple; results_on_new_tab=0; doi_resolver=oadoi.org; simple_style=auto; center_alignment=0; advanced_search=0; query_in_title=0; infinite_scroll=0; search_on_category_select=1; hotkeys=default; url_formatting=pretty; disabled_engines="wikipedia__general\054currency__general\054wikidata__general\054duckduckgo__general\054google__general\054lingva__general\054startpage__general\054dictzone__general\054mymemory translated__general\054brave__general"; enabled_engines="baidu__general\054bing__general\054sogou__general"; disabled_plugins=; enabled_plugins=; tokens=
```

添加Cookie后，就能正常使用。