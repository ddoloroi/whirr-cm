/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.whirr.cm.server;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cloudera.whirr.cm.server.CmServerService.CmServerServiceStatus;
import com.cloudera.whirr.cm.server.impl.CmServerFactory;
import com.cloudera.whirr.cm.server.impl.CmServerImpl;

public class CmServerClusterTest extends BaseTestServer {

  private CmServerCluster cluster;

  @Before
  public void setupCluster() throws CmServerException {
    cluster = new CmServerCluster();
    cluster.setServer(new CmServerServiceBuilder().ip("192.168.0.1").build());
    cluster.addAgent(new CmServerServiceBuilder().host("some-host").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_SECONDARY_NAMENODE).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HUE_SERVER).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HUE_BEESWAX_SERVER).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_DATANODE).tag(CLUSTER_TAG)
        .qualifier("2").host("host-2").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_DATANODE).tag(CLUSTER_TAG)
        .qualifier("3").host("host-3").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_DATANODE).tag(CLUSTER_TAG)
        .qualifier("4").host("host-4").build());
  }

  @Test
  public void testVersion() throws CmServerException {
    boolean caught = false;
    Assert.assertEquals("" + CmServerImpl.VERSION_CM_API_MATRIX_CM_MAX_MAJOR,
        new CmServerFactory().getCmServer("", "", 7180, "", "", null).getVersion());
    Assert.assertEquals("" + CmServerImpl.VERSION_CM_API_MATRIX_CM_MAX_MAJOR,
        new CmServerFactory().getCmServer(null, "", "", 7180, "", "", null).getVersion());
    Assert.assertEquals("" + CmServerImpl.VERSION_CM_API_MATRIX_CM_MAX_MAJOR,
        new CmServerFactory().getCmServer("", "", "", 7180, "", "", null).getVersion());
    Assert.assertEquals("5", new CmServerFactory().getCmServer("5", "", "", 7180, "", "", null).getVersion());
    Assert.assertEquals("5.0.0", new CmServerFactory().getCmServer("5.0.0", "", "", 7180, "", "", null).getVersion());
    Assert.assertEquals("5.1.0", new CmServerFactory().getCmServer("5.1.0", "", "", 7180, "", "", null).getVersion());
    caught = false;
    try {
      Assert.assertNull(new CmServerFactory().getCmServer("-1", "", "", 7180, "", "", null).getVersion());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertNull(new CmServerFactory().getCmServer("0", "", "", 7180, "", "", null).getVersion());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertNull(new CmServerFactory().getCmServer("3", "", "", 7180, "", "", null).getVersion());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertNull(new CmServerFactory().getCmServer("3.6", "", "", 7180, "", "", null).getVersion());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertNull(new CmServerFactory().getCmServer("4.0", "", "", 7180, "", "", null).getVersion());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertNull(new CmServerFactory().getCmServer("10", "", "", 7180, "", "", null).getVersion());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertNull(new CmServerFactory().getCmServer("ABC", "", "", 7180, "", "", null).getVersion());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
  }

  @Test
  public void testVersionApi() throws CmServerException {
    boolean caught = false;
    Assert.assertEquals(CmServerImpl.VERSION_CM_API_MATRIX_API_MAX,
        new CmServerFactory().getCmServer("", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(CmServerImpl.VERSION_CM_API_MATRIX_API_MAX,
        new CmServerFactory().getCmServer(null, "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(CmServerImpl.VERSION_CM_API_MATRIX_API_MAX,
        new CmServerFactory().getCmServer("", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(3, new CmServerFactory().getCmServer(null, "3", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(4, new CmServerFactory().getCmServer(null, "4", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(3, new CmServerFactory().getCmServer("", "3", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(4, new CmServerFactory().getCmServer("", "4", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(3, new CmServerFactory().getCmServer("5", "3", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(6, new CmServerFactory().getCmServer("5", null, "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(6, new CmServerFactory().getCmServer("5", "6", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(6, new CmServerFactory().getCmServer("5.0.0", "6", "", "", 7180, "", "", null).getVersionApi());
    Assert.assertEquals(6, new CmServerFactory().getCmServer("5.1.0", "6", "", "", 7180, "", "", null).getVersionApi());
    caught = false;
    try {
      Assert.assertEquals(4, new CmServerFactory().getCmServer("3", "", "", 7180, "", "", null).getVersionApi());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertEquals(4, new CmServerFactory().getCmServer("ABC", "", "", 7180, "", "", null).getVersionApi());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertEquals(4, new CmServerFactory().getCmServer("4.0", "4", "", "", 7180, "", "", null).getVersionApi());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertEquals(4, new CmServerFactory().getCmServer("4.5", "4", "", "", 7180, "", "", null).getVersionApi());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertEquals(4, new CmServerFactory().getCmServer("4", "10", "", "", 7180, "", "", null).getVersionApi());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertEquals(1, new CmServerFactory().getCmServer("4.6", "1", "", "", 7180, "", "", null).getVersionApi());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert
          .assertEquals(1, new CmServerFactory().getCmServer("10.6", "4", "", "", 7180, "", "", null).getVersionApi());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
  }

  @Test
  public void testVersionCdh() throws CmServerException {
    boolean caught = false;
    Assert.assertEquals(CmServerImpl.VERSION_CDH_MAX, new CmServerFactory().getCmServer("", "", 7180, "", "", null)
        .getVersionCdh());
    Assert.assertEquals(CmServerImpl.VERSION_CDH_MAX,
        new CmServerFactory().getCmServer(null, null, null, "", "", 7180, "", "", null).getVersionCdh());
    Assert.assertEquals(CmServerImpl.VERSION_CDH_MAX,
        new CmServerFactory().getCmServer(null, null, "", "", "", 7180, "", "", null).getVersionCdh());
    Assert.assertEquals(4, new CmServerFactory().getCmServer(null, null, "4", "", "", 7180, "", "", null)
        .getVersionCdh());
    Assert.assertEquals(5, new CmServerFactory().getCmServer(null, null, "5", "", "", 7180, "", "", null)
        .getVersionCdh());
    caught = false;
    try {
      Assert.assertEquals("CDH3", new CmServerFactory().getCmServer(null, null, "3", "", "", 7180, "", "", null)
          .getVersionCdh());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertEquals(10, new CmServerFactory().getCmServer(null, null, "10", "", "", 7180, "", "", null)
          .getVersionCdh());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
  }

  @Test
  public void testConfiguration() throws CmServerException {
    CmServerCluster cluster = new CmServerCluster();
    cluster.addServiceConfiguration("3", CmServerServiceType.HDFS_NAMENODE.getId(), "some-setting-3", "some-value-3");
    cluster.addServiceConfiguration("4", CmServerServiceType.HDFS_NAMENODE.getId(), "some-setting-4", "some-value-4");
    Assert
        .assertEquals(
            "some-value-3",
            cluster.getServiceConfiguration().get("3").get(CmServerServiceType.HDFS_NAMENODE.getId())
                .get("some-setting-3"));
    Assert
        .assertEquals(
            "some-value-4",
            cluster.getServiceConfiguration().get("4").get(CmServerServiceType.HDFS_NAMENODE.getId())
                .get("some-setting-4"));
    Assert.assertEquals("some-value-3",
        cluster.getServiceConfiguration(3).get(CmServerServiceType.HDFS_NAMENODE.getId()).get("some-setting-3"));
    Assert.assertEquals(null,
        cluster.getServiceConfiguration(3).get(CmServerServiceType.HDFS_NAMENODE.getId()).get("some-setting-4"));
    Assert.assertEquals("some-value-3",
        cluster.getServiceConfiguration(4).get(CmServerServiceType.HDFS_NAMENODE.getId()).get("some-setting-3"));
    Assert.assertEquals("some-value-4",
        cluster.getServiceConfiguration(4).get(CmServerServiceType.HDFS_NAMENODE.getId()).get("some-setting-4"));
    Assert.assertEquals("some-value-3",
        cluster.getServiceConfiguration(10).get(CmServerServiceType.HDFS_NAMENODE.getId()).get("some-setting-3"));
    Assert.assertEquals("some-value-4",
        cluster.getServiceConfiguration(10).get(CmServerServiceType.HDFS_NAMENODE.getId()).get("some-setting-4"));
  }

  @Test
  public void testService() throws CmServerException {
    Assert.assertTrue(new CmServerServiceBuilder().type(CmServerServiceType.CLUSTER).build()
        .equals(new CmServerServiceBuilder().build()));
    Assert.assertTrue(new CmServerServiceBuilder().type(CmServerServiceType.CLUSTER).tag(CLUSTER_TAG).build()
        .equals(new CmServerServiceBuilder().tag(CLUSTER_TAG).build()));
    Assert.assertTrue(new CmServerServiceBuilder().host("host").ip(null).build()
        .equals(new CmServerServiceBuilder().host("host").build()));
    Assert.assertTrue(new CmServerServiceBuilder()
        .name(
            CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM + CmServerServiceType.HDFS_NAMENODE.toString().toLowerCase()
                + CmServerService.NAME_TOKEN_DELIM + CmServerService.NAME_QUALIFIER_DEFAULT)
        .host("host")
        .ip("127.0.0.1")
        .status(CmServerServiceStatus.UNKNOWN)
        .build()
        .equals(
            new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE).tag(CLUSTER_TAG)
                .qualifier(CmServerService.NAME_QUALIFIER_DEFAULT).host("host").ip("127.0.0.1").build()));
    boolean caught = false;
    try {
      Assert.assertTrue(new CmServerServiceBuilder()
          .name("")
          .host("host")
          .status(CmServerServiceStatus.UNKNOWN)
          .build()
          .equals(
              new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE).tag(CLUSTER_TAG)
                  .qualifier(CmServerService.NAME_QUALIFIER_DEFAULT).host("host").ip("127.0.0.1").build()));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      Assert.assertTrue(new CmServerServiceBuilder()
          .name(
              CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM + CmServerService.NAME_TOKEN_DELIM
                  + CmServerServiceType.HDFS_NAMENODE.toString().toLowerCase() + CmServerService.NAME_TOKEN_DELIM
                  + CmServerService.NAME_QUALIFIER_DEFAULT)
          .host("host")
          .status(CmServerServiceStatus.UNKNOWN)
          .build()
          .equals(
              new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE)
                  .tag(CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM)
                  .qualifier(CmServerService.NAME_QUALIFIER_DEFAULT).host("host").ip("127.0.0.1").build()));
    } catch (IllegalArgumentException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
  }

  @Test
  public void testIsEmpty() throws CmServerException {
    CmServerCluster cluster = new CmServerCluster();
    Assert.assertTrue(cluster.isEmpty());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    Assert.assertTrue(cluster.isEmpty());
    cluster.setServer(new CmServerServiceBuilder().ip("192.168.0.1").build());
    Assert.assertTrue(cluster.isEmpty());
    cluster.addAgent(new CmServerServiceBuilder().host("some-host").build());
    Assert.assertFalse(cluster.isEmpty());
  }

  @Test
  public void testIsValid() {
    Assert.assertTrue(CmServerServiceType.MAPREDUCE.isValid(3, 4));
    Assert.assertTrue(CmServerServiceType.MAPREDUCE.isValid(4, 4));
    Assert.assertTrue(CmServerServiceType.MAPREDUCE.isValid(10, 4));
    Assert.assertFalse(CmServerServiceType.MAPREDUCE.isValid(10, 5));
    Assert.assertFalse(CmServerServiceType.YARN.isValid(3, 4));
    Assert.assertFalse(CmServerServiceType.YARN.isValid(4, 4));
    Assert.assertFalse(CmServerServiceType.YARN.isValid(10, 4));
    Assert.assertTrue(CmServerServiceType.YARN.isValid(10, 5));
  }

  @Test
  public void testAdd() throws CmServerException {
    boolean caught = false;
    try {
      cluster.addServiceType(CmServerServiceType.CLUSTER);
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      cluster.addServiceType(CmServerServiceType.HDFS);
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.CLUSTER).tag(CLUSTER_TAG).build());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS).tag(CLUSTER_TAG).build());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      cluster.addServiceType(CmServerServiceType.HDFS_NAMENODE);
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
    caught = false;
    try {
      cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE).tag(CLUSTER_TAG).build());
    } catch (CmServerException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
  }

  @Test
  public void testGetTypes() throws CmServerException {
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS, CmServerServiceType.HUE }, cluster
        .getServiceTypes().toArray());
  }

  @Test
  public void testGetServiceTypes() throws InterruptedException, IOException, CmServerException {
    Assert.assertEquals(5, cluster.getServiceTypes(CmServerServiceType.CLUSTER).size());
    Assert.assertEquals(3, cluster.getServiceTypes(CmServerServiceType.HDFS).size());
    Assert.assertEquals(1, cluster.getServiceTypes(CmServerServiceType.HDFS_NAMENODE).size());
    Assert.assertEquals(1, cluster.getServiceTypes(CmServerServiceType.HDFS_DATANODE).size());
    Assert.assertEquals(0, cluster.getServiceTypes(CmServerServiceType.CLIENT).size());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_SECONDARY_NAMENODE, CmServerServiceType.HDFS_DATANODE, CmServerServiceType.HUE_SERVER,
        CmServerServiceType.HUE_BEESWAX_SERVER }, cluster.getServiceTypes(CmServerServiceType.CLUSTER).toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_SECONDARY_NAMENODE, CmServerServiceType.HDFS_DATANODE },
        cluster.getServiceTypes(CmServerServiceType.HDFS).toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_SECONDARY_NAMENODE, CmServerServiceType.HDFS_DATANODE },
        cluster.getServiceTypes(CmServerServiceType.HDFS, 3, 4).toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_SECONDARY_NAMENODE, CmServerServiceType.HDFS_DATANODE },
        cluster.getServiceTypes(CmServerServiceType.HDFS, 4, 4).toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_SECONDARY_NAMENODE, CmServerServiceType.HDFS_DATANODE, CmServerServiceType.HUE_SERVER,
        CmServerServiceType.HUE_BEESWAX_SERVER }, cluster.getServiceTypes(CmServerServiceType.CLUSTER, 6, 4).toArray());
    Assert.assertArrayEquals(
        new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE, CmServerServiceType.HDFS_SECONDARY_NAMENODE,
            CmServerServiceType.HDFS_DATANODE, CmServerServiceType.HUE_SERVER },
        cluster.getServiceTypes(CmServerServiceType.CLUSTER, 6, 5).toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] {}, cluster.getServiceTypes(CmServerServiceType.HDFS, 0, 4)
        .toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] {}, cluster.getServiceTypes(CmServerServiceType.HDFS, 2, 4)
        .toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] {}, cluster.getServiceTypes(CmServerServiceType.HDFS, 4, 0)
        .toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] {}, cluster.getServiceTypes(CmServerServiceType.HDFS, 4, 3)
        .toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] {}, cluster.getServiceTypes(CmServerServiceType.HDFS, 0, 0)
        .toArray());
    Assert.assertArrayEquals(new CmServerServiceType[] {}, cluster.getServiceTypes(CmServerServiceType.HDFS, 2, 3)
        .toArray());
    CmServerCluster cluster = new CmServerCluster();
    cluster.setServer(new CmServerServiceBuilder().ip("192.168.0.1").build());
    cluster.addAgent(new CmServerServiceBuilder().host("some-host").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.MAPREDUCE_JOB_TRACKER).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_DATANODE).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.MAPREDUCE_TASK_TRACKER).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_DATANODE, CmServerServiceType.MAPREDUCE_JOB_TRACKER,
        CmServerServiceType.MAPREDUCE_TASK_TRACKER }, cluster.getServiceTypes(CmServerServiceType.CLUSTER).toArray());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.YARN_JOB_HISTORY).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_DATANODE, CmServerServiceType.YARN_JOB_HISTORY },
        cluster.getServiceTypes(CmServerServiceType.CLUSTER, 6, 5).toArray());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.MAPREDUCE_JOB_TRACKER).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_DATANODE, CmServerServiceType.YARN_JOB_HISTORY },
        cluster.getServiceTypes(CmServerServiceType.CLUSTER, 6, 5).toArray());
    cluster = new CmServerCluster();
    cluster.setServer(new CmServerServiceBuilder().ip("192.168.0.1").build());
    cluster.addAgent(new CmServerServiceBuilder().host("some-host").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.HDFS_NAMENODE).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    cluster.addService(new CmServerServiceBuilder().type(CmServerServiceType.YARN_JOB_HISTORY).tag(CLUSTER_TAG)
        .qualifier("1").host("host-1").build());
    Assert.assertArrayEquals(new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.YARN_JOB_HISTORY }, cluster.getServiceTypes(CmServerServiceType.CLUSTER).toArray());
  }

  @Test
  public void testGetServices() throws InterruptedException, IOException {
    Assert.assertEquals(7, cluster.getServices(CmServerServiceType.CLUSTER).size());
    Assert.assertEquals(5, cluster.getServices(CmServerServiceType.HDFS).size());
    Assert.assertEquals(1, cluster.getServices(CmServerServiceType.HDFS_NAMENODE).size());
    Assert.assertEquals(3, cluster.getServices(CmServerServiceType.HDFS_DATANODE).size());
    Assert.assertEquals(0, cluster.getServices(CmServerServiceType.CLIENT).size());
    int i = 0;
    CmServerServiceType[] serviceTypes = new CmServerServiceType[] { CmServerServiceType.HDFS_NAMENODE,
        CmServerServiceType.HDFS_SECONDARY_NAMENODE, CmServerServiceType.HDFS_DATANODE,
        CmServerServiceType.HDFS_DATANODE, CmServerServiceType.HDFS_DATANODE, CmServerServiceType.HUE_SERVER,
        CmServerServiceType.HUE_BEESWAX_SERVER };
    for (CmServerService service : cluster.getServices(CmServerServiceType.CLUSTER)) {
      Assert.assertEquals(serviceTypes[i++], service.getType());
    }
    Assert.assertEquals(7, i);
    i = 0;
    for (CmServerService service : cluster.getServices(CmServerServiceType.HDFS)) {
      Assert.assertEquals(serviceTypes[i++], service.getType());
    }
    Assert.assertEquals(5, i);
  }

  @Test
  public void testGetService() throws InterruptedException, IOException {
    Assert.assertNotNull(cluster.getService(CmServerServiceType.CLUSTER));
    Assert.assertNotNull(cluster.getService(CmServerServiceType.HDFS));
    Assert.assertNotNull(cluster.getService(CmServerServiceType.HDFS_NAMENODE));
    Assert.assertNotNull(cluster.getService(CmServerServiceType.HDFS_DATANODE));
    Assert.assertNull(cluster.getService(CmServerServiceType.CLIENT));
  }

  @Test
  public void testGetNames() throws InterruptedException, IOException {
    Assert.assertEquals(CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM
        + CmServerServiceType.CLUSTER.toString().toLowerCase() + CmServerService.NAME_TOKEN_DELIM + "1",
        cluster.getServiceName(CmServerServiceType.CLUSTER));
    Assert.assertEquals(cluster.setName("My Cluster - Against Conventions!"),
        cluster.getServiceName(CmServerServiceType.CLUSTER));
    Assert.assertEquals(CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM
        + CmServerServiceType.HDFS.toString().toLowerCase() + CmServerService.NAME_TOKEN_DELIM + "1",
        cluster.getServiceName(CmServerServiceType.HDFS));
    Assert.assertEquals(CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM
        + CmServerServiceType.HDFS_NAMENODE.toString().toLowerCase() + CmServerService.NAME_TOKEN_DELIM + "1",
        cluster.getServiceName(CmServerServiceType.HDFS_NAMENODE));
    Assert.assertEquals(CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM
        + CmServerServiceType.HDFS_DATANODE.toString().toLowerCase() + CmServerService.NAME_TOKEN_DELIM + "1",
        cluster.getServiceName(CmServerServiceType.HDFS_DATANODE));
    Assert.assertEquals(CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM
        + CmServerServiceType.CLIENT.toString().toLowerCase() + CmServerService.NAME_TOKEN_DELIM + "1",
        cluster.getServiceName(CmServerServiceType.CLIENT));
    boolean caught = false;
    try {
      Assert.assertEquals(CLUSTER_TAG + CmServerService.NAME_TOKEN_DELIM
          + CmServerServiceType.CLUSTER.toString().toLowerCase() + CmServerService.NAME_TOKEN_DELIM + "1",
          new CmServerCluster().getServiceName(CmServerServiceType.CLUSTER));
    } catch (IOException e) {
      caught = true;
    }
    Assert.assertTrue(caught);
  }

}
