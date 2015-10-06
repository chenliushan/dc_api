package comp.domain;

import java.util.List;

/**
 * Created by liushanchen on 15/10/5.
 */
public class SinaMetadata {
    public String size;// "0 bytes",
    public String rev;// "b3dc232e",
    public String thumb_exists;// false,
    public String bytes;// "0",
    public String modified;// "Mon, 05 Oct 2015 07:47:54 +0000",
    public String path;//"/",
    public String is_dir;// true,
    public String root;// "sandbox",
    public String icon;// "folder",
    public String revision;// "500921058",
    public String is_deleted;// false,
    public String hash;// "708eae299f7fbaf2544c4df9b0a62419",
    public String mime_type;// "text/plain",
    public String md5;// "9b1040870e8fd76d6d097d65444cf6bf",
    public String sha1;// "1eb9beabcf43384ba27751239364a821fd8c9f61"
    public List<SinaMetadata> contents;
    /*新浪metadata返回格式例子：
"size": "0 bytes",
  "rev": "5dd24202",
  "thumb_exists": false,
  "bytes": "0",
  "modified": "Tue, 06 Oct 2015 11:45:26 +0000",
  "path": "/",
  "is_dir": true,
  "root": "sandbox",
  "icon": "folder",
  "revision": "500921058",
  "is_deleted": false,
  "hash": "84d4d52aaae8304842ea730faa87f6c2",
  "contents": [
    {
      "size": "3.19 KB",
      "rev": "3750418607",
      "thumb_exists": false,
      "bytes": "3268",
      "modified": "Tue, 06 Oct 2015 11:45:26 +0000",
      "path": "/500921058",
      "is_dir": false,
      "root": "sandbox",
      "icon": "page_white",
      "mime_type": "application/octet-stream",
      "revision": "3032578704",
      "md5": "7cfb8d67388dfda7b5481c0a22bac124",
      "sha1": "4bfab45ba81dcf14f5121ccbba1c31d8b31eb264",
      "is_deleted": false
 */

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getThumb_exists() {
        return thumb_exists;
    }

    public void setThumb_exists(String thumb_exists) {
        this.thumb_exists = thumb_exists;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIs_dir() {
        return is_dir;
    }

    public void setIs_dir(String is_dir) {
        this.is_dir = is_dir;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }


    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public void setContents(List<SinaMetadata> contents) {
        this.contents = contents;
    }
}
